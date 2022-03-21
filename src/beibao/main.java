package 背包问题;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;

public class main extends JPanel {
    static Scanner Cin=new Scanner(System.in);
    static int W[];  //重量
    static int V[];  //价值
    static int S[];
    static double PCR[]; //Price-Cost Ratio
    static int m,n;
    static int TotalV=0;

    /*
     数据读入与处理
     */
    public static void ReadFile() {
        int Option;
        System.out.println("数据输入 :");
        Option=Cin.nextInt();
        if (Option>10 || Option<0) {
            System.out.println("无需要输入的数据");
        }
        String Data="shuju\\beibao"+String.valueOf(Option)+".in";
        System.out.println(Data);
        try {
            Scanner In=new Scanner(new FileReader(Data));
            m=In.nextInt();
            n=In.nextInt();
            W=new int[10010];
            V=new int[10010];
            S=new int[10010];
            PCR=new double[10010];
            for (int i=1;i<=n;i++) {
                W[i] = In.nextInt();
                V[i] = In.nextInt();
                S[i] = i;
                TotalV+=V[i];
                PCR[i] = (double) V[i] / (double) W[i];
                System.out.printf("重量: %4d 价值: %4d 性价比: %4.3f\n", W[i], V[i], PCR[i]);
            }
        } catch (IOException e) {
            System.out.println("无文件");
        }
    }

    /*
     int类型数据交换
     */
    public static void SInt(int Data[],int a,int b) {
        int temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    /*
     double类型数据交换
     */
    public static void SDouble(double Data[],int a,int b) {
        double temp=Data[a];
        Data[a]=Data[b];
        Data[b]=temp;
    }

    /*
     排序
     */
    public static void DataSort() {
        for (int i=1;i<=n-1;i++) {
            for (int j=i+1;j<=n;j++) {
                if (PCR[i]<PCR[j]) {
                    SDouble(PCR,i,j);
                    SInt(W,i,j);
                    SInt(V,i,j);
                    SInt(S,i,j);
                }
            }
        }
    }

    final int Space=20;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D=(Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int Width=getWidth();
        int Height=getHeight();
        g2D.draw(new Line2D.Double(Space,Space,Space,Height-Space)); //绘制x轴
        g2D.draw(new Line2D.Double(Space,Height-Space,Width-Space,Height-Space)); //绘制y轴
        Font font=new Font("Microsoft YaHei UI",Font.PLAIN,10); //修改字体
        g2D.setFont(font);
        g2D.drawString("0",Space-10,Height-Space+10); //添加文字
        g2D.drawString("W",Width-Space-20,Height-Space+10);
        g2D.drawString("V",Space-10,Space-5);
        double xAxis=(double)(Width-2*Space)/getMaxWeight();
        double yAxis=(double)(Height-2*Space)/getMaxValue();
        g2D.setPaint(Color.blue);
        for (int i=1;i<=n;i++) { //开始绘制点
            double x=Space+xAxis*W[i];
            double y=Height-Space-yAxis*V[i];
            g2D.fill(new Ellipse2D.Double(x-2,y-2,4,4));
        }
    }

    /*
     获取最大重量
     */
    private int getMaxWeight() {
        int MaxW=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (W[i]>MaxW) MaxW=W[i];
        }
        return MaxW;
    }

    /*
     获取最大价值
     */
    private int getMaxValue() {
        int MaxV=-Integer.MAX_VALUE;
        for (int i=1;i<=n;i++) {
            if (V[i]>MaxV) MaxV=V[i];
        }
        return MaxV;
    }

    /*
     散点图绘制
     */
    public static void PlottingScatterPlots() {
        JFrame Frame=new JFrame();
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(new main());
        Frame.setSize(400,400);
        Frame.setLocation(330,330);
        Frame.setVisible(true);
    }

    static int Res;
    static int Vectors[];

    /*
     贪心算法
     */
    public static void Greedy() {
        Vectors=new int[10010];
        for (int i=1;i<=n;i++) Vectors[i]=0;
        int Size=m;
        int Ans=0;
        for (int i=1;i<=n;i++) {
            if (Size>W[i]) {
                Size-=W[i];
                Ans+=V[i];
                Vectors[S[i]]=1;
            } else {
                break;
            }
        }
        Res=Ans;
        System.out.println("解:"+Ans);
        System.out.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Vectors[i]+",");
            else System.out.println(Vectors[i]+"}");
        }
    }

    static int Path[];
    static int f[];

    public static void FindPath(int Size) {
        while (Size>0) {
            for (int i=1;i<=n;i++) {
                if (Path[S[i]]==0) {
                    if (Size-W[i]>=0) {
                        if (f[Size-W[i]]+V[i]==f[Size]) {
                            Path[S[i]]=1;
                            Size-=W[i];
                            break;
                        }
                    }
                }
            }
            Size--;
        }
    }

    /*
     动态规划算法
     */
    public static void DP() {
        f=new int[10010];
        for (int i=1;i<=n;i++) {
            for (int j=m;j>=W[i];j--) {
                f[j]=Math.max(f[j],f[j-W[i]]+V[i]); 
            }
        }
        Res=f[m];
        System.out.println("解:"+f[m]);
        Path=new int[10010];
        for (int i=1;i<=n;i++) Path[i]=0;
        FindPath(m);
        System.out.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) System.out.print(Path[i]+",");
            else System.out.println(Path[i]+"}");
        }
    }

    static int Ans=0;
    static int CW=0; //当前重量
    static int CV=0; //当前价值
    static int Flag[];

    public static double Bound(int Index) {
        double RemainW=m-CW;
        double CurrentV=CV;
        while (Index<n && W[Index]<=RemainW) {
            RemainW-=W[Index];
            CurrentV+=V[Index];
            Index++;
        }
        if (Index<=n) {
            CurrentV+=PCR[Index]*RemainW;
        }
        return CurrentV;
    }

    /*
     回溯算法
     */
    public static void BackTrack(int Index) {
        if (Index>n) {
            Ans=CV;
            return;
        }
        if (CW+W[Index]<=m) {
            CW+=W[Index];
            CV+=V[Index];
            Flag[S[Index]]=1;
            BackTrack(Index+1);
            CW-=W[Index];
            CV-=V[Index];
        }
        if (Bound(Index+1)>Ans) {
            BackTrack(Index+1);
        }
    }

    static int ArcAns;

    /*
     文件保存
     */
    public static void WriteFile(int Ans, double RunTime, int AnsRoute[]) throws FileNotFoundException {
        PrintStream Cout=new PrintStream("res.txt");
        ArcAns=Ans;
        Cout.println("解: "+Ans);
        Cout.println("时间: "+RunTime+"s");
        Cout.print("解向量: {");
        for (int i=1;i<=n;i++) {
            if (i!=n) Cout.print(AnsRoute[i]+",");
            else Cout.println(AnsRoute[i]+"}");
        }
        Cout.close();
    }

    public static void SelectSolution() {
        System.out.println("算法选择");
        System.out.println("1:贪心算法\t2:动态规划算法\t3:回溯算法");
        int Operation;
        Operation=Cin.nextInt();
        double RunTime=0.0;
        if (Operation==1) {
            long StartTime=System.nanoTime();
            Greedy();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Vectors);
            } catch (IOException e) {

            }
        }
        if (Operation==2) {
            long StartTime=System.nanoTime();
            DP();
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            System.out.println("时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Path);
            } catch (IOException e) {

            }
        }
        if (Operation==3) {
            Flag=new int[10010];
            for (int i=1;i<=n;i++) Flag[i]=0;
            long StartTime=System.nanoTime();
            BackTrack(1);
            long EndTime=System.nanoTime();
            RunTime=(EndTime-StartTime)/1000000000.0;
            Res=Ans;
            System.out.print("解向量: {");
            for (int i=1;i<=n;i++) {
                if (i!=n) System.out.print(Flag[i]+",");
                else System.out.println(Flag[i]+"}");
            }
            System.out.println("解: "+Ans);
            System.out.println("时间: "+RunTime+"s");
            try {
                WriteFile(Res,RunTime,Flag);
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadFile();
        PlottingScatterPlots();
        DataSort();
        SelectSolution();
    }
}
