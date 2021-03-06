import java.util.*;
import java.lang.*;
import java.io.*;
 
 
public class Game {
	
	Board sudoku;
	
	public class Cell{
		private int row = 0;
		private int column = 0;
		
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		public int getRow() {
			return row;
		}
		public int getColumn() {
			return column;
		}
	}
	
	public class Region{
		private Cell[] matrix;
		private int num_cells;
		public Region(int num_cells) {
			this.matrix = new Cell[num_cells];
			this.num_cells = num_cells;
		}
		public Cell[] getCells() {
			return matrix;
		}
		public void setCell(int pos, Cell element){
			matrix[pos] = element;
		}
 
	}
	
	public class Board{
		private int[][] board_values;
		private Region[] board_regions;
		private int num_rows;
		private int num_columns;
		private int num_regions;
		
		public Board(int num_rows,int num_columns, int num_regions){
			this.board_values = new int[num_rows][num_columns];
			this.board_regions = new Region[num_regions];
			this.num_rows = num_rows;
			this.num_columns = num_columns;
			this.num_regions = num_regions;
		}
		
		public int[][] getValues(){
			return board_values;
		}
		public int getValue(int row, int column) {
			return board_values[row][column];
		}
		public Region getRegion(int index) {
			return board_regions[index];
		}
		public Region[] getRegions(){
			return board_regions;
		}
		public void setValue(int row, int column, int value){
			board_values[row][column] = value;
		}
		public void setRegion(int index, Region initial_region) {
			board_regions[index] = initial_region;
		}	
		public void setValues(int[][] values) {
			board_values = values;
		}
		public Region getRegionFromCoOrdinate(int r, int c){
			for (int i = 0; i<num_regions; i++) {
				for (int j=0; j<board_regions[i].num_cells; j++) {
					if (board_regions[i].matrix[j].row==r && board_regions[i].matrix[j].column==c) {
						return board_regions[i];
					}
				}
			}
			return null;
		}
	}
	private boolean checkAdjacent(int row, int column, int n) {
		if (row!=0) {
			if (n == sudoku.getValue(row-1, column)) {
				return false;
			}
		}
		if (column!=0) {
			if (n == sudoku.getValue(row, column-1)) {
				return false;
			}
		}
		if (row!=0 && column!=0) {
			if (n == sudoku.getValue(row-1, column-1)) {
				return false;
			}
		}
		if (row!=0 && column!=sudoku.num_columns-1) {
			if (n == sudoku.getValue(row-1, column+1)) {
				return false;
			}
		}
		if (row!=sudoku.num_rows-1 && column!=0) {
			if (n == sudoku.getValue(row+1, column-1)) {
				return false;
			}
		}
		if (row!=sudoku.num_rows-1) {
			if (n == sudoku.getValue(row+1, column)) {
				return false;
			}
		}
		if (column!=sudoku.num_columns-1) {
			if (n == sudoku.getValue(row, column+1)) {
				return false;
			}
		}
		if (row!=sudoku.num_rows-1 && column!=sudoku.num_columns-1) {
			if (n == sudoku.getValue(row+1, column+1)) {
				return false;
			}
		} 
		return true;
	}
	
	private boolean checkRegion(int row, int col, int n) {
		for (int k=0; k<sudoku.getRegionFromCoOrdinate(row,col).matrix.length; k++) {
			if(n == sudoku.getValue(sudoku.getRegionFromCoOrdinate(row,col).matrix[k].getRow(), sudoku.getRegionFromCoOrdinate(row,col).matrix[k].getColumn())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isValid(int row, int col, int n) {
		return checkAdjacent(row, col, n) && checkRegion(row,  col, n);
	}
	
	private boolean solverHelper(int row, int col) { 
		if (row==sudoku.num_rows-1 && col == sudoku.num_columns) {
			return true;
		}
		if (col == sudoku.num_columns) {
            row++;
            col = 0;
        }
		if (sudoku.getValue(row, col)!=-1) {
            return solverHelper(row, col+1);
		}
		
		for (int n=1; n<=sudoku.getRegionFromCoOrdinate(row, col).num_cells; n++) {
				if (isValid(row,col,n)) {
					sudoku.setValue(row,col,n);
				if (solverHelper(row,col+1)){
					return true;
					}
				}
				sudoku.setValue(row, col, -1);
			}
			return false;
		}
		
	
	
	public int[][] solver() {
		//To Do => Please start coding your solution here
		if (solverHelper(0,0)) {
			return sudoku.getValues();
		}
			return sudoku.getValues();
	}
 
	
	public static void main(String[] args) {
		System.out.println();
		Scanner sc = new Scanner(System.in);
		int rows = sc.nextInt();
		int columns = sc.nextInt();
		int[][] board = new int[rows][columns];
		//Reading the board
		for (int i=0; i<rows; i++){
			for (int j=0; j<columns; j++){
				String value = sc.next();
				if (value.equals("-")) {
					board[i][j] = -1;
				}else {
					try {
						board[i][j] = Integer.valueOf(value);
					}catch(Exception e) {
						System.out.println("Ups, something went wrong");
					}
				}	
			}
		}
		int regions = sc.nextInt();
		Game game = new Game();
	    game.sudoku = game.new Board(rows, columns, regions);
		game.sudoku.setValues(board);
		for (int i=0; i< regions;i++) {
			int num_cells = sc.nextInt();
			Game.Region new_region = game.new Region(num_cells);
			for (int j=0; j< num_cells; j++) {
				String cell = sc.next();
				String value1 = cell.substring(cell.indexOf("(") + 1, cell.indexOf(","));
				String value2 = cell.substring(cell.indexOf(",") + 1, cell.indexOf(")"));
				Game.Cell new_cell = game.new Cell(Integer.valueOf(value1)-1,Integer.valueOf(value2)-1);
				new_region.setCell(j, new_cell);
			}
			game.sudoku.setRegion(i, new_region);
		}
		int[][] answer = game.solver();
		for (int i=0; i<answer.length;i++) {
			for (int j=0; j<answer[0].length; j++) {
				System.out.print(answer[i][j]);
				if (j<answer[0].length -1) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
 
 
}
