package com.tugo.learn

object EightQueen {

  class Board(val size : Int) {
    // Get the next position, return None, if no next position is available
    def next(pos : (Int, Int)) : Option[(Int, Int)] = {
      if (pos._2 < size-1) Some((pos._1, pos._2 + 1))
      None
    }

    // check if current position is valid, compare to other positions
    def valid(state: Set[(Int, Int)], pos: (Int, Int)) : Boolean = {
      def conflict(c1 : (Int, Int)): Boolean =
        (c1._2 == pos._2 || c1._1 == pos._1 || Math.abs(c1._1 - pos._1) == Math.abs(c1._2 - pos._2))
      state exists conflict
    }

    def find_solution(state: Set[(Int, Int)], pos : (Int, Int)): Option[Set[(Int, Int)]] = {
      if (state.size >= size-1) return Some(state)
      if (valid(state, pos)) return find_solution(state + pos, (pos._1 + 1, 0))
      None
    }

    def find_solution() : Option[Set[(Int, Int)]] = find_solution(Set(), (0,0))
  }


  def main(args : Array[String]): Unit = {

  }

}
