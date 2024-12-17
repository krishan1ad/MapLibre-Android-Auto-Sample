package nl.flitsmeister.car_common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import androidx.car.app.CarContext
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import nl.flitsmeister.car_common.extentions.appManager
import nl.flitsmeister.car_common.extentions.runOnMainThread
import org.maplibre.android.maps.MapView

class CarMapRenderer(
    private val carContext: CarContext,
    serviceLifecycle: Lifecycle
) : SurfaceCallback, DefaultLifecycleObserver, ICarMapRenderer {

    // The map container used to handle the map lifecycle
    private val mapContainer = CarMapContainer(carContext, serviceLifecycle)

    private val osmPaint = Paint().apply {
        color = carContext.getColor(R.color.osm_attribution)
        textAlign = Paint.Align.RIGHT
        typeface = Typeface.DEFAULT
    }

    // The surface to draw the map container on
    private var surfaceContainer: SurfaceContainer? = null

    // Handler to post actions to the UI thread
    private val uiHandler = Handler(Looper.getMainLooper())

    // The last known stable area
    private var lastKnownStableArea = Rect()
    private var lastKnownVisibleArea = Rect()

    init {
        serviceLifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        try {
            carContext.appManager.setSurfaceCallback(this)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Could not set surface callback", e)
            return
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.v(LOG_TAG, "CarMapRenderer.onDestroy")
        surfaceContainer = null
        uiHandler.removeCallbacksAndMessages(null)
        try {
            carContext.appManager.setSurfaceCallback(null)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Could not remove surface callback", e)
        }
    }

    override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
        Log.v(LOG_TAG, "CarMapRenderer.onSurfaceAvailable")
        this.surfaceContainer = surfaceContainer
        mapContainer.setSurfaceSize(surfaceContainer.width, surfaceContainer.height)
        mapContainer.mapViewInstance?.apply {
            addOnDidBecomeIdleListener { drawOnSurface() }
            addOnWillStartRenderingFrameListener {
                drawOnSurface()
            }
        }
        runOnMainThread {
            // Start drawing the map on the android auto surface
            drawOnSurface()
        }
    }

    private fun drawOnSurface() {
        val mapView = mapContainer.mapViewInstance ?: return
        val surface = surfaceContainer?.surface ?: return

        val canvas = surface.lockHardwareCanvas()
        drawMapOnCanvas(mapView, canvas)
        surface.unlockCanvasAndPost(canvas)
    }

    private fun drawMapOnCanvas(mapView: MapView, canvas: Canvas) {
        val mapViewTextureView = mapView.takeIf { it.childCount > 0 }?.getChildAt(0) as? TextureView

        mapViewTextureView?.bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        val density = carContext.resources.displayMetrics.density

        canvas.drawText(
            carContext.getString(R.string.copyright_openstreetmap),
            canvas.width - (12 * density),
            canvas.height - (4 * density),
            osmPaint.apply {
                textSize = 12 * density
            }
        )
    }

    override fun onVisibleAreaChanged(visibleArea: Rect) {
        if (visibleArea != lastKnownVisibleArea) {
            Log.v(
                LOG_TAG,
                "onVisibleAreaChanged left(${visibleArea.left}) top(${visibleArea.top}) right(${visibleArea.right}) bottom(${visibleArea.bottom})"
            )
            lastKnownVisibleArea = visibleArea
        }
    }

    override fun onStableAreaChanged(stableArea: Rect) {
        if (stableArea != lastKnownStableArea) {
            Log.v(
                LOG_TAG,
                "onStableAreaChanged left(${stableArea.left}) top(${stableArea.top}) right(${stableArea.right}) bottom(${stableArea.bottom})"
            )
            //if only the vertical space has changed, you can ignore this mostly.
            lastKnownStableArea = stableArea
        }
    }

    override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
        Log.v(LOG_TAG, "Surface destroyed")
        this.surfaceContainer = null
        uiHandler.removeCallbacksAndMessages(null)
    }

    override fun zoomInFromButton() {
        val centerX = surfaceContainer?.width?.toFloat()?.div(2) ?: -1f
        val centerY = surfaceContainer?.height?.toFloat()?.div(2) ?: -1f
        onScale(centerX, centerY, CarMapContainer.DOUBLE_CLICK_FACTOR)
    }

    override fun zoomOutFromButton() {
        val centerX = surfaceContainer?.width?.toFloat()?.div(2) ?: -1f
        val centerY = surfaceContainer?.height?.toFloat()?.div(2) ?: -1f
        onScale(centerX, centerY, -CarMapContainer.DOUBLE_CLICK_FACTOR)
    }

    //Map interactivity
    override fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
        mapContainer.onScale(focusX, focusY, scaleFactor)
    }

    @Synchronized
    override fun onScroll(distanceX: Float, distanceY: Float) {
        Log.v(LOG_TAG, "onScroll distanceX($distanceX) distanceY($distanceY)")
        mapContainer.scrollBy(distanceX, distanceY)
    }

    override fun onClick(x: Float, y: Float) {
        super.onClick(x, y)
    }

    override fun onFling(velocityX: Float, velocityY: Float) {
        super.onFling(velocityX, velocityY)
        // We don't need to implement onFling since the MapView does this for us
    }

    companion object {
        const val LOG_TAG = "CarMapRenderer"
    }
}