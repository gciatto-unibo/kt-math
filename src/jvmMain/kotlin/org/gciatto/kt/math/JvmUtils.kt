@file:JvmName("JvmUtils")

package org.gciatto.kt.math

import kotlin.random.Random
import kotlin.random.asJavaRandom
import java.math.BigDecimal as JavaBigDecimal
import java.math.BigInteger as JavaBigInteger
import java.math.MathContext as JavaMathContext
import java.math.RoundingMode as JavaRoundingMode

internal actual fun logImpl(lazyObject: () -> Any) {
    System.err.println(lazyObject())
}

fun BigInteger.toJava(): JavaBigInteger = when (this) {
    is JavaBigIntegerAdapter -> value
    else -> JavaBigInteger(this.toByteArray())
}

fun BigDecimal.toJava(): JavaBigDecimal = when (this) {
    is JavaBigDecimalAdapter -> value
    else -> JavaBigDecimal(unscaledValue.toJava(), scale, JavaMathContext(precision, JavaRoundingMode.UNNECESSARY))
}

fun JavaRoundingMode.toKotlin(): RoundingMode = RoundingMode.valueOf(ordinal)

fun RoundingMode.toJava(): JavaRoundingMode = JavaRoundingMode.valueOf(oldMode)

fun JavaMathContext.toKotlin(): MathContext = MathContext(precision, roundingMode.toKotlin())

fun MathContext.toJava(): JavaMathContext = JavaMathContext(precision, roundingMode.toJava())

fun fromJava(value: JavaBigInteger): BigInteger = JavaBigIntegerAdapter(value)

fun JavaBigInteger.toKotlin(): BigInteger = fromJava(this)

fun fromJava(value: JavaBigDecimal): BigDecimal = JavaBigDecimalAdapter(value)

fun JavaBigDecimal.toKotlin(): BigDecimal = fromJava(this)

internal actual inline fun <T, reified U : T> T?.castTo(): U = this as U

internal actual fun bigProbablePrimeInteger(bitLength: Int, rnd: Random): BigInteger =
    JavaBigIntegerAdapter(JavaBigInteger.probablePrime(bitLength, rnd.asJavaRandom()))

internal actual fun bigIntegerOf(value: Long): BigInteger = JavaBigIntegerAdapter(JavaBigInteger.valueOf(value))

internal actual fun bigIntegerOf(value: String): BigInteger = JavaBigIntegerAdapter(JavaBigInteger(value))

internal actual fun bigIntegerOf(value: Int): BigInteger =
    JavaBigIntegerAdapter(JavaBigInteger.valueOf(value.toLong()))

internal actual fun bigIntegerOf(value: String, radix: Int): BigInteger =
    JavaBigIntegerAdapter(JavaBigInteger(value, radix))

internal actual fun bigIntegerOf(value: IntArray): BigInteger =
    JavaBigIntegerAdapter(CommonBigInteger.of(value).toJava())

internal actual fun bigDecimalOf(unscaledVal: Long, scale: Int): BigDecimal =
    JavaBigDecimalAdapter(JavaBigDecimal.valueOf(unscaledVal, scale))

internal actual fun bigDecimalOf(unscaledVal: Long, scale: Int, prec: Int): BigDecimal =
    JavaBigDecimalAdapter(
        JavaBigDecimal(
            JavaBigInteger.valueOf(unscaledVal),
            scale,
            JavaMathContext(prec, JavaRoundingMode.UNNECESSARY)
        )
    )

internal actual fun bigDecimalOf(`val`: Int): BigDecimal =
    JavaBigDecimalAdapter(JavaBigDecimal.valueOf(`val`.toLong()))

internal actual fun bigDecimalOf(`val`: Long): BigDecimal =
    JavaBigDecimalAdapter(JavaBigDecimal.valueOf(`val`))

internal actual fun bigDecimalOf(intVal: BigInteger, scale: Int, prec: Int): BigDecimal =
    JavaBigDecimalAdapter(
        JavaBigDecimal(
            intVal.toJava(),
            scale,
            JavaMathContext(prec, JavaRoundingMode.UNNECESSARY)
        )
    )

internal actual fun bigDecimalOf(`val`: Double, ctx: MathContext?): BigDecimal =
    if (ctx == null) {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`))
    } else {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`, ctx.toJava()))
    }


internal actual fun bigDecimalOf(`val`: Float, ctx: MathContext?): BigDecimal =
    bigDecimalOf(`val`.toDouble(), ctx)

internal actual fun bigDecimalOf(`val`: String, ctx: MathContext?): BigDecimal =
    if (ctx == null) {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`))
    } else {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`, ctx.toJava()))
    }

internal actual fun bigDecimalOf(`val`: BigInteger, ctx: MathContext?): BigDecimal =
    if (ctx == null) {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`.toJava()))
    } else {
        JavaBigDecimalAdapter(JavaBigDecimal(`val`.toJava(), ctx?.toJava()))
    }

internal actual fun bigDecimalOf(`val`: Int, ctx: MathContext): BigDecimal =
    bigDecimalOf(`val`.toLong(), ctx)

internal actual fun bigDecimalOf(`val`: Long, ctx: MathContext): BigDecimal =
    JavaBigDecimalAdapter(JavaBigDecimal(`val`, ctx.toJava()))
