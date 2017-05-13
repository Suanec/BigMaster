import breeze.linalg.{
  DenseMatrix => BDM,
  DenseVector => BDV,
  pinv
}
import breeze.numerics.pow
object BCD_one {
  type SDM = BDM[Double]
  def shape( m : BDM[_] ) = m.rows -> m.cols
  def subColMatrix( m : SDM, idx : Array[Int] ) : SDM = idx.map( i => m(::,i to i)).reduceLeft((x,y) => BDM.horzcat(x,y))
  def subRowMatrix( m : SDM, idx : Array[Int] ) : SDM = idx.map( i => m(i to i, ::)).reduceLeft((x,y) => BDM.vertcat(x,y))
  def subMatrix( m : SDM, rowIdx : Array[Int] = Array(), colIdx : Array[Int] = Array()) : SDM = {
    val subByRow = (rowIdx.size > 0) match {
      case true => subRowMatrix(m,rowIdx)
      case false => m
    }
    val subByCol = (colIdx.size > 0) match {
      case true => subColMatrix(subByRow,colIdx)
      case false => subByRow
    }
    subByCol
  }
  def upZeros( v : BDV[Double] ) : Array[Int] = v.toArray.zipWithIndex.filter(_._1 > 0).map(_._2)
  def nonZeros( m : SDM ) = m.pairs.filter(_._2 != 0)
  def upZeros( m : SDM ) = m.pairs.filter(_._2 > 0)
  // # R = 100*120
  // # U = 100*4
  // # V = 120*4
  // # theta = 120*4
  def BCD_one(
    R : SDM,
    U : SDM,
    V : SDM,
    theta : SDM,
    lambda_u : Double = 100d,
    lambda_v : Double = 0.1,
    dirSave : String = ".",
    getLoss : Boolean = false,
    numIter : Int = 1) = {
    val UT = U.t 
    println(R.data.filter(_ != 0).size)
    val VT = V.t 
    val thetaT = theta.t 
    val numU = R.rows
    val numV = R.cols
    val K = UT.rows
    val a = 1d
    val b = 0.01
    val aMB = a - b
    val IU = BDM.eye[Double](K):*lambda_u
    val IV = BDM.eye[Double](K)*lambda_v
    val C = (BDM.ones[Double](numU,numV) * b)
    val E = UT.t * VT - R
    upZeros(R).foreach((x,y) => C(x._1,x._2) = a)
    (0 until numIter).map{
      it =>
        val uSquare = UT * U * b
        (0 until numV).map{
          j =>
            val idx = upZeros(R(::,j))
            val U_cut = subColMatrix(UT,idx)
            VT(::,j) := (pinv( uSquare + U_cut * U_cut.t * aMB + IV ) * (U_cut * subMatrix(R,idx,Array(j)) + thetaT(::, j to j ) * lambda_v)).toDenseVector
        }
        val vSquare = VT * V * b
        (0 until numU).map{
          i =>
            val idx = upZeros(R(i,::).t)
            val V_cut = subColMatrix(VT,idx)
            UT(::,i) := (pinv( vSquare + V_cut * V_cut.t * aMB + IU)*(V_cut * subMatrix(R,Array(i),idx).t)).toDenseVector
        }
        if(it % 10 == 9) {
          E := UT.t * VT - R 
          val error : Double = (C :* pow(E,2)).sum / 2d
          val regLossV : Double = pow((thetaT - VT),2).sum / 2d
          val regLossU : Double = pow(UT,2).sum / 2d
          val loss : Double = error + lambda_v * regLossV + lambda_u * regLossU
          println(s"iter ${it} : Loss : " + "%.3f".format(loss))
        }
    }
    val loss = getLoss match {
      case true => {
        // E := UT.t * VT - R 
        // val error = (C :* pow(E,2)).sum / 2d
        // val regLossV = pow((thetaT - VT),2).sum / 2d
        // val regLossU = pow(UT,2).sum / 2d
        // error + lambda_v * regLossV + lambda_u * regLossU
      }
      case false => 0
    }
    (UT.t, VT.t, loss)
  }
}

