package com.ymc.kotlindemo

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView

/**
 * 自定义圆形 ImageView
 *
 * @packageName: com.ymc.kotlindemo
 * @fileName: RoundImageView
 * @date: 2019/5/22  16:48
 * @author: ymc
 * @QQ:745612618
 */

/**
 * 当主构造器 有注解 或者 可见性修饰符 必须加上 constructor
 */
class RoundImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null,
                                               defAttrStyle: Int = 0) : ImageView(context, attributeSet, defAttrStyle) {
    /**
     * 图片类型
     */
    enum class ShapeType {
        SHAPE_CIRCLE,
        SHAPE_ROUND
    }

    //重写set 方法 更新参数并更新画布
    private var mShapeType: ShapeType = ShapeType.SHAPE_CIRCLE
        set(value) {
            field = value
            invalidate()
        }
    /**
     * 框 宽度
     */
    private var mBorderWidth: Float = 20f
        set(value) {
            field = value
            invalidate()
        }
    /**
     * 框 颜色
     */
    private var mBorderColor: Int = Color.parseColor("#ff9900")
        set(value) {
            field = value
            invalidate()
        }
    /**
     * 是否显示 边框
     */
    private var mShowBorder: Boolean = true
        set(value) {
            field = value
            invalidate()
        }
    private var mShowCircleDot: Boolean = false
        set(value) {
            field = value
            invalidate()
        }
    private var mCircleDotColor: Int = Color.RED
        set(value) {
            field = value
            invalidate()
        }
    private var mCircleDotRadius: Float = 20f
        set(value) {
            field = value
            invalidate()
        }
    private var mLeftTopRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mLeftTopRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mRightTopRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mRightTopRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mLeftBottomRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mLeftBottomRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mRightBottomRadiusX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var mRightBottomRadiusY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    //drawTools var
    private lateinit var mShapePath: Path
    private lateinit var mBorderPath: Path
    private lateinit var mBitmapPaint: Paint
    private lateinit var mBorderPaint: Paint
    private lateinit var mCircleDotPaint: Paint
    private lateinit var mMatrix: Matrix

    //temp var
    private var mWidth: Int = 200//View的宽度
    private var mHeight: Int = 200//View的高度
    private var mRadius: Float = 100f//圆的半径

    init {
        initAttrs(context, attributeSet, defAttrStyle)//获取自定义属性的值
        initDrawTools()//初始化绘制工具
    }

    /**
     * 获取自定义属性值
     */
    private fun initAttrs(context: Context, attributeSet: AttributeSet?, defAttrStyle: Int) {
        val arr = context.obtainStyledAttributes(attributeSet,
                R.styleable.RoundImageView, defAttrStyle, 0)
        (0..arr.indexCount).asSequence().map { arr.getIndex(it) }.forEach {
            when (it) {
                R.styleable.RoundImageView_shape_type ->
                    mShapeType = when {
                        arr.getInt(it, 0) == 0 -> ShapeType.SHAPE_CIRCLE
                        arr.getInt(it, 0) == 1 -> ShapeType.SHAPE_ROUND
                        else -> ShapeType.SHAPE_CIRCLE
                    }
                R.styleable.RoundImageView_border_width ->
                    mBorderWidth = arr.getDimension(it,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics))
                R.styleable.RoundImageView_border_color ->
                    mBorderColor = arr.getColor(it, Color.parseColor("#ff0000"))
                R.styleable.RoundImageView_left_top_radiusX ->
                    mLeftTopRadiusX = arr.getDimension(it, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_left_top_radiusY ->
                    mLeftTopRadiusY = arr.getDimension(it,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    0f,resources.displayMetrics))
                R.styleable.RoundImageView_right_top_radiusX ->
                        mRightTopRadiusX = arr.getDimension(it,TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_right_top_radiusY ->
                        mRightTopRadiusY = arr.getDimension(it,TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_left_bottom_radiusX ->
                    mLeftBottomRadiusX = arr.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_left_bottom_radiusY ->
                    mLeftBottomRadiusY = arr.getDimension(it, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_right_bottom_radiusX ->
                        mRightBottomRadiusX = arr.getDimension(it,TypedValue.applyDimension
                        (TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_right_bottom_radiusY ->
                    mRightBottomRadiusY = arr.getDimension(it, TypedValue.applyDimension
                    (TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics))
                R.styleable.RoundImageView_show_border ->
                    mShowBorder = arr.getBoolean(it, false)
                R.styleable.RoundImageView_show_circle_dot ->
                    mShowCircleDot = arr.getBoolean(it, false)
                R.styleable.RoundImageView_circle_dot_color ->
                    mCircleDotColor = arr.getColor(it, Color.parseColor("#ff0000"))
                R.styleable.RoundImageView_circle_dot_radius ->
                    mCircleDotRadius = arr.getDimension(it, TypedValue.applyDimension
                    (TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics))
            }
        }
        arr.recycle()
    }

    /**
     * 初始化 绘画工具
     * apply 内联扩展函数 返回值为对象本身 (主要用于对象初始化)
     * run 返回值为 函数最后一行  （结果返回）
     */
    private fun initDrawTools() {
        //需要设置BitmapShader着色器
        mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        //绘制边框画笔
        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = mBorderColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = mBorderWidth
        }
        //绘制右上角圆点画笔
        mCircleDotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = mCircleDotColor
        }
        //形状轮廓的path路径
        mShapePath = Path()
        //图片边框轮廓的path路径
        mBorderPath = Path()
        //缩放图片的矩阵
        mMatrix = Matrix()
        scaleType = ScaleType.CENTER_CROP
    }

    //View的测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mShapeType == ShapeType.SHAPE_CIRCLE) {
            mWidth = Math.min(measuredWidth, measuredHeight)
            mRadius = mWidth / 2.0f
            setMeasuredDimension(mWidth, mWidth)
        } else {
            mWidth = measuredWidth
            mHeight = measuredHeight
            setMeasuredDimension(mWidth, mHeight)
        }
    }

    //确定了最终View的尺寸
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBorderPath.reset()
        mShapePath.reset()
        when (mShapeType) {
            ShapeType.SHAPE_ROUND -> {
                mWidth = w
                mHeight = h
                buildRoundPath()
            }
            ShapeType.SHAPE_CIRCLE -> {
                buildCirclePath()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {//由于经过以上根据不同逻辑构建了boderPath和shapePath,path中已经储存相应的形状，现在只需要把相应shapePath中形状用带BitmapShader画笔绘制出来,boderPath用普通画笔绘制出来即可
        drawable ?: return
        //获得相应的BitmapShader着色器对象
        mBitmapPaint.shader = getBitmapShader()
        when (mShapeType) {
            ShapeType.SHAPE_CIRCLE -> {
                if (mShowBorder) {
                    canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆形图片边框path
                }
                canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆形图片形状path
                if (mShowCircleDot) {
                    drawCircleDot(canvas)//绘制圆形图片右上角圆点
                }
            }
            ShapeType.SHAPE_ROUND -> {
                if (mShowBorder) {
                    canvas?.drawPath(mBorderPath, mBorderPaint)//绘制圆角图片边框path
                }
                canvas?.drawPath(mShapePath, mBitmapPaint)//绘制圆角图片形状path
            }
        }

    }

    private fun drawCircleDot(canvas: Canvas?) {
        canvas?.run {
            drawCircle((mRadius + mRadius * (Math.sqrt(2.0) / 2.0f)).toFloat(), (mRadius - mRadius * (Math.sqrt(2.0) / 2.0f)).toFloat(), mCircleDotRadius, mCircleDotPaint)
        }
    }

    private fun getBitmapShader(): BitmapShader {
        val bitmap = drawableToBitmap(drawable)
        return BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
            var scale = 1.0f
            if (mShapeType == ShapeType.SHAPE_CIRCLE) {
                scale = (mWidth * 1.0f / Math.min(bitmap.width, bitmap.height))
            } else if (mShapeType == ShapeType.SHAPE_ROUND) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                if (!(width == bitmap.width && width == bitmap.height)) {
                    scale = Math.max(width * 1.0f / bitmap.width, height * 1.0f / bitmap.height)
                }
            }
            // shader的变换矩阵，我们这里主要用于放大或者缩小
            mMatrix.setScale(scale, scale)
            setLocalMatrix(mMatrix)
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        return Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(Canvas(this@apply))
        }
    }

    /**
     * 绘制 圆形类型的
     */
    private fun buildCirclePath() {
        if (!mShowBorder) {//绘制不带边框的圆形
            mShapePath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW)
        } else {//绘制带边框的圆形需要把内部圆形和外部圆形边框都要扔进path
            mShapePath.addCircle(mRadius, mRadius, mRadius - mBorderWidth, Path.Direction.CW)
            mBorderPath.addCircle(mRadius, mRadius, mRadius - mBorderWidth / 2.0f, Path.Direction.CW)
        }
    }


    /**
     * 绘制圆角类型的
     */
    private fun buildRoundPath() {
        //圆角矩形
        if (!mShowBorder) {
            floatArrayOf(mLeftTopRadiusX, mLeftTopRadiusY,
                    mRightTopRadiusX, mRightTopRadiusY,
                    mRightBottomRadiusX, mRightBottomRadiusY,
                    mLeftBottomRadiusX, mLeftBottomRadiusY).run {
                mShapePath.addRoundRect(RectF(0f, 0f, mWidth.toFloat(),
                        mHeight.toFloat()), this, Path.Direction.CW)
            }
        }else {//绘制带边框的圆角实际上只需要把一个圆角矩形和一个圆角矩形的变量都扔进path即可
            floatArrayOf(mLeftTopRadiusX - mBorderWidth / 2.0f, mLeftTopRadiusY - mBorderWidth / 2.0f,
                    mRightTopRadiusX - mBorderWidth / 2.0f, mRightTopRadiusY - mBorderWidth / 2.0f,
                    mRightBottomRadiusX - mBorderWidth / 2.0f, mRightBottomRadiusY - mBorderWidth / 2.0f,
                    mLeftBottomRadiusX - mBorderWidth / 2.0f, mLeftBottomRadiusY - mBorderWidth / 2.0f).run {
                mBorderPath.addRoundRect(RectF(mBorderWidth / 2.0f, mBorderWidth / 2.0f, mWidth.toFloat() - mBorderWidth / 2.0f, mHeight.toFloat() - mBorderWidth / 2.0f), this, Path.Direction.CW)
            }

            floatArrayOf(mLeftTopRadiusX - mBorderWidth, mLeftTopRadiusY - mBorderWidth,
                    mRightTopRadiusX - mBorderWidth, mRightTopRadiusY - mBorderWidth,
                    mRightBottomRadiusX - mBorderWidth, mRightBottomRadiusY - mBorderWidth,
                    mLeftBottomRadiusX - mBorderWidth, mLeftBottomRadiusY - mBorderWidth).run {
                mShapePath.addRoundRect(RectF(mBorderWidth, mBorderWidth, mWidth.toFloat() - mBorderWidth, mHeight.toFloat() - mBorderWidth),
                        this, Path.Direction.CW)
            }
        }
    }


}
