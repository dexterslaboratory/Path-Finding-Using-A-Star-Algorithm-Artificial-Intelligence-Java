import java.io.*;
import java.util.LinkedList;
public class AStar {
	public static LinkedList<Cell> open = new LinkedList<Cell>();
	public static LinkedList<Cell> closed = new LinkedList<Cell>();
	public static String b;
	public static void main(String args[])throws IOException{
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
	System.out.println("Enter the dimensions:");
	String input = br.readLine();
	String lnb[]=input.split(" ");
	int row=Integer.parseInt(lnb[0]);
	int col=Integer.parseInt(lnb[1]);
	System.out.println("Enter source:");
	int source = Integer.parseInt(br.readLine());
	System.out.println("Enter destination:");
	int destination = Integer.parseInt(br.readLine());
	System.out.println("Enter blocked cells separated by space:");
	b = br.readLine();
	b=" "+b+" ";
	Cell c[][]=new Cell[row][col];
	
	initialiseCell(c,row,col);	//****do not forget to initialise**//
	prepareGrid(c,row,col,source,destination);
	computeheuristic(c,destination,row,col);
	putInOpen(c,source,row,col);//put source in open list
	
	
	while(pathFinding(c,row,col));
	for(int i=0;i<row;i++){
		for(int j = 0; j<col ; j++){
			System.out.print(c[i][j].parent+"\t");
		}System.out.println();
	}
	printPath(c,destination,row,col);
	}
	public static boolean pathFinding(Cell c[][],int row,int col){
		if(open.isEmpty()){return false;}
		Cell minfCell = pullCellWithMinf();//find then transfer cell with minimun f value from open to closed list;
		int xOfMin = minfCell.x;
		int yOfMin = minfCell.y;
		int mask[][]={{-1,-1},{1,1},{-1,1},{1,-1},{-1,0},{1,0},{0,-1},{0,1}};
		for(int i=0;i<8;i++){
			int maskx=mask[i][0];
			int masky=mask[i][1];
			if(maskx+xOfMin<row  && maskx+xOfMin>=0 && masky+yOfMin<col && masky+yOfMin>=0){
				int neighbourx = maskx+xOfMin;
				int neighboury = masky+yOfMin;
				if(c[neighbourx][neighboury].g==0 && c[neighbourx][neighboury].f==0 && !c[neighbourx][neighboury].blocked){
					//the above if condition checks if the cell is yet untouched or not.
					if(i==0||i==1||i==2||i==3){
						c[neighbourx][neighboury].g = c[xOfMin][yOfMin].g + 14;
					}
					else{
						c[neighbourx][neighboury].g = c[xOfMin][yOfMin].g + 10;
					}
					c[neighbourx][neighboury].parent = c[xOfMin][yOfMin].num;
					putInOpen(c,c[neighbourx][neighboury].num,row,col);
				}
			}
		}
		return true;
	}
	public static void printPath(Cell c[][],int destination,int row,int col){
		int xny[] = getCoordinates(destination, row, col);
		int x = xny[0];int y = xny[1];
		Cell s = c[x][y];
		System.out.print("The path is: "+s.num+" ");
		while(!s.source){
			System.out.print("<- "+s.parent);
			int XnY[]=getCoordinates(s.parent,row,col);
			int X = XnY[0];int Y = XnY[1];
			s = c[X][Y];
		}
	}
	public static Cell pullCellWithMinf(){
		int minf = open.getFirst().f;
		Cell p = new Cell();
		for(Cell c:open){
			if(c.f<minf)minf=c.f;
		}
		for(Cell c:open){
			if(c.f==minf){
				p=c;
				break;
				}
		}
		open.remove(p);
		closed.add(p);
		//System.out.println("P returned");
		return p;
	}
	public static void putInOpen(Cell c[][],int source,int row,int col){
	int sxny[]=getCoordinates(source, row, col);
	int x=sxny[0];
	int y=sxny[1];
	c[x][y].f = c[x][y].h+c[x][y].g;//compute f before putting it in open list
	open.add(c[x][y]);
	}
	public static void initialiseCell(Cell c[][],int row,int col){
		for(int i=0;i<row;i++){
			for(int j = 0; j<col ; j++){
				c[i][j] = new Cell();
			}
		}
	}
	public static void computeheuristic(Cell c[][],int destination,int row,int col){
		int dxny[] = getCoordinates(destination, row, col);
		int x=dxny[0];
		int y=dxny[1];
	//	System.out.println("col "+col);
		for(int i=0;i<row;i++){
			for(int j=0; j<col ; j++){
				c[i][j].h = Math.abs(i-x)+Math.abs(j-y);
			}
		}
	}
	public static int[] getCoordinates(int num,int row,int col){
		int x = (num-1)/col;
		int y = (num-1)%col;
		int xny[]={x,y};
		return xny;
	}
	public static void prepareGrid(Cell c[][],int row,int col,int source,int destination){
		int count = 0;
		for(int i=0;i<row;i++){
			for(int j = 0; j<col ; j++){
				c[i][j].num = ++count;
				c[i][j].x=i;
				c[i][j].y=j;
				if (b.indexOf(" "+c[i][j].num+" ")!= -1 ){c[i][j].blocked = true;}
				if (c[i][j].num==source)c[i][j].source = true;
				if (c[i][j].num==destination)c[i][j].destination = true;
			}
		}
	}
	public static void initialiseBlockedArray(String b[],int blocked[]){
		for(int i=0;i<b.length;i++){
			blocked[i] = Integer.parseInt(b[i]);
		}
	}
	public static class Cell{
		boolean source = false;
		boolean destination=false;
		boolean blocked = false;
		int g=0;
		int h=0;
		int f=0;
		int x=0;
		int y=0;
		int num=0;
		int parent=0;
	}
}
