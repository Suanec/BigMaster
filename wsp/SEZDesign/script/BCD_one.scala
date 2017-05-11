
import breeze.linalg.{DenseMatrix => BDM}
import breeze.numerics.pow
type SDM = BDM[Double]
def shape( m : BDM[_] ) = m.rows -> m.cols

/// val R = BDM.rand[Double](10,7).map( x => if(x >= 0.9) 1d else 0d)
def BCD_one(
  R : SDM,
  U : SDM,
  V : SDM,
  theta : SDM,
  lambda_u : SDM,
  lambda_v : SDM,
  dirSave : String = ".",
  getLoss : Boolean = False,
  numIter : Int = 1) = {
  val UT = U.t 
  println(R.data.filter(_ != 0).size)
  val VT = V.t 
  val thetaT = theta.t 
  val numU = R.rows
  val numV = R.cols
  val K = UT.rows
  val a = 1
  val b = 0.01
  val aMB = a - b
  val IU = BDM.eye[Double](K)*lambda_u
  val IV = BDM.eye[Double](K)*lambda_v

  /// confidence parameter
  val valueR = R.map(x => if(x > 0) a else 0d)
  val emptyR = R.map(x => if(x > 0) 0 else x * b )
  val C = valueR + emptyR
  
  (0 until numIter).map{
    it =>
      val uSquare = UT * U * b
      (0 until numV).map{
        j =>  
          val idx = 

      }
  }
  if(getLoss) {
    var E = (C * pow((UT.t * VT -R),2)).sum / 2d
    var regLossV = pow((theta - VT),2).sum / 2d
    var regLossU = pow(UT,2).sum / 2d
    E = E + lambda_v * regLossV + lambda_u * regLossU
  } else E = 0
  (UT, VT, E)
}