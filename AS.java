import java.io.*;
import java.util.*;
public class AS {
	public static int row,col,s,d;
	public static cell grid[][];
	public static int blocked[];
	public static LinkedList<cell> open = new LinkedList<cell>();
	public static LinkedList<cell> closed = new LinkedList<cell>();
	public static void main(String args[])throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter order of the matrix");
		String order = br.readLine();
		String order1[] = order.split(" ");
		row = Integer.parseInt(order1[0]);
		col = Integer.parseInt(order1[1]);
		System.out.println("Enter source and destination");
		String snd[] = br.readLine().split(" ");
		s = Integer.parseInt(snd[0]);
		d = Integer.parseInt(snd[1]);
		System.out.println("Enter blocked states");
		String block[]=br.readLine().split(" ");
		blocked = new int[block.length];
		for(int i=0;i<blocked.length;i++){
			blocked[i]=Integer.parseInt(block[i]);
		}
		grid = new cell[row][col];
		initialiseGrid();
		prepareGrid();
		putInOpen(s);
		
		while(pathing());
		printPath();
		
	}
	public static void printPath(){
		int x=(d-1)/col;
		int y=(d-1)%col;
		cell current = grid[x][y];
		System.out.print(d);
		while(!current.source){
			int c = current.parent;
			System.out.print("<-"+c);
			x = (c-1)/col;
			y = (c-1)%col;
			current = grid[x][y];
		}//System.out.print("<-"+s);
	}
	public static void initialiseGrid(){
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				grid[i][j] = new cell();
			}
		}
	}
	public static boolean pathing(){
		if(open.isEmpty())return false;
		cell cMinF = pullMinFCell();
		open.remove(cMinF);
		closed.add(cMinF);
		int x=cMinF.i;
		int y=cMinF.j;
		//compute g of neighbours of cMinF
		int vector[][]={{-1,-1},{-1,1},{1,1},{1,-1},{-1,0},{1,0},{0,-1},{0,1}};
		for(int i=0;i<8;i++){
		if(x+vector[i][0]>=0 && x+vector[i][0]<row && y+vector[i][1]>=0 && y+vector[i][1]<col){
		   int neighbourx = x+vector[i][0];
		   int neighboury = y+vector[i][1];
		   if(grid[neighbourx][neighboury].g==0 && grid[neighbourx][neighboury].f==0 
				   && !grid[neighbourx][neighboury].blocked){
		   if(i<4){
			   grid[neighbourx][neighboury].g = grid[x][y].g + 14;
		   			}
		   else{
			   grid[neighbourx][neighboury].g = grid[x][y].g+10;
			   }
		   grid[neighbourx][neighboury].parent = grid[x][y].num;
		   putInOpen(grid[neighbourx][neighboury].num);
															}
		}
			}
		return true;
		
	}
	public static cell pullMinFCell(){
		cell min = open.getFirst();
		int minf = min.f;
		for(cell c:open){
			if(c.f<minf)minf=c.f;
		}
		for(cell c:open){
			if(c.f==minf){min=c;break;}
		}
		return min;
	} 
	public static void putInOpen(int num){
		int x=(num-1)/col;
		int y=(num-1)%col;
		//System.out.println(x+" "+y);
		grid[x][y].f=grid[x][y].h+grid[x][y].g;
		open.add(grid[x][y]);
		//System.out.println("Open "+open);
		
	}
	public static void prepareGrid(){
		int destRow =0;
		int destCol =0;
		//number the cells
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				grid[i][j].i=i;
				grid[i][j].j=j;
				grid[i][j].num = (i*col+j)+1;
			}
		}
		//set source, destination and blocked
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				if(grid[i][j].num==s)grid[i][j].source = true ;
				if(grid[i][j].num==d){grid[i][j].destination = true;destRow=i;destCol=j;}
				if(isBlocked(grid[i][j].num)){grid[i][j].blocked=true;}
			}
		}
		//compute h
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				grid[i][j].h = Math.abs(i-destRow)+Math.abs(j-destCol);
			}
		}
		
		
		
	}
	public static boolean isBlocked(int num){
		for(int i =0; i<blocked.length;i++){
			if(num == blocked[i])return true;
		}
		return false;
	}
	public static class cell {
		boolean blocked = false;
		boolean source = false;
		boolean destination = false;
		int num =0;
		int h=0;
		int i=0;
		int j=0;
		int f=0;
		int g=0;
		int parent = 0;
	}
}
