package com.company;
import java.util.*;

class queen {
    public int[][] convert(List<List<String>> res) {
        int[][] c = new int[res.size()][res.get(0).size()];
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(0).size(); j++) {
                for (int k = 0; k < res.get(0).get(0).length(); k++) {
                    if (res.get(i).get(j).charAt(k) == 'Q') {
                        c[i][j] = k;
                    }
                }
            }
        }
        return c;
    }
    private static List<String> charToString(char[][] array) {
        List<String> result = new LinkedList<>();
        for (char[] chars : array) {
            result.add(String.valueOf(chars));
        }
        return result;
    }
    List<List<String>> res = new ArrayList<>();
    public int[][] QueenMain(int n) {
        char[][] desk = new char[n][n];
        for (char[] fill : desk) {
            Arrays.fill(fill, '.');
        }
        backtrack(desk, 0);
        return convert(res);
    }
    public void backtrack(char[][] desk, int row) {
        if (row == desk[0].length) {
            res.add(charToString(desk));
            return;
        }
        int n = desk[0].length;
        for (int col = 0; col < n; col++) {
            if (!exertQueen(desk, row, col)) {
                continue;
            }
            desk[row][col] = 'Q';
            backtrack(desk, row + 1);
            desk[row][col] = '.';
        }
    }
    public boolean exertQueen(char[][] desk, int row, int col) {
        for (char[] chars : desk) {
            if (chars[col] == 'Q') {
                return false;
            }
        }
        for (int i = row - 1, j = col + 1; i >= 0 && j < desk.length; i--, j++) {
            if (desk[i][j] == 'Q') {
                return false;
            }
        }
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (desk[i][j] == 'Q') {
                return false;
            }
        }
        return true;
    }
}

class backpack {
    int[][] dynamic(int[] weight, int[] value, int capacity, int num) {
        int[][] res = new int[num + 1][capacity + 1];
        for (int i = 1; i <= num; i++) {
            for (int j = 1; j <= capacity; j++) {
                if (weight[i-1] > j) {
                    res[i][j] = res[i - 1][j];
                }
                else {
                    res[i][j] = Math.max(res[i - 1][j], res[i - 1][j - weight[i-1]] + value[i-1]);
                }
            }
        }
        return res;
    }
}

class PackNode implements Comparable<PackNode>{
    int weight;
    double value;
    double upValue;
    int Left;
    int level;
    PackNode father;
    public int compareTo(PackNode node){
        return Double.compare(node.upValue, this.upValue);
    }
}

class BranchAndBound {
    static int W;
    static int n;
    static int []w;
    static double []v;
    static int maxValue;
    static int[] bestWay;
    public static void setvalue(int W, int n, int[] w, double[] v) {
        BranchAndBound.W = W;
        BranchAndBound.n = n;
        BranchAndBound.w = w;
        BranchAndBound.v = v;
        BranchAndBound.bestWay = new int[n];
    }
    static void getMaxValue(){
        PriorityQueue<PackNode> pq = new PriorityQueue<PackNode>();
        PackNode initial = new PackNode();
        initial.level = -1;
        for (double value : v) initial.upValue += value;
        pq.add(initial);
        while(!pq.isEmpty()){
            PackNode fatherNode = pq.poll();
            if(fatherNode.level == n-1){
                if(fatherNode.value > maxValue){
                    maxValue = (int)fatherNode.value;
                    for(int i=n-1;i>=0;i--){
                        bestWay[i] = fatherNode.Left;
                        fatherNode = fatherNode.father;
                    }
                }
            }
            else{
                if(w[fatherNode.level+1]+fatherNode.weight <= W){
                    PackNode newNode = new PackNode();
                    newNode.level = fatherNode.level+1;
                    newNode.value = fatherNode.value + v[fatherNode.level+1];
                    newNode.weight = w[fatherNode.level+1]+fatherNode.weight;
                    newNode.upValue = Bound(newNode);
                    newNode.father = fatherNode;
                    newNode.Left = 1;
                    if(newNode.upValue > maxValue)
                        pq.add(newNode);
                }
                if((fatherNode.upValue - v[fatherNode.level+1])> maxValue){
                    PackNode newNode2 = new PackNode();
                    newNode2.level = fatherNode.level+1;
                    newNode2.value = fatherNode.value;
                    newNode2.weight = fatherNode.weight;
                    newNode2.father = fatherNode;
                    newNode2.upValue = fatherNode.upValue - v[fatherNode.level+1];
                    newNode2.Left = 0;
                    pq.add(newNode2);
                }

            }
        }
    }
    static double Bound(PackNode node){
        double maxLeft = node.value;
        int leftWeight = W - node.weight;
        int templevel = node.level;
        while(templevel <= n-1 && leftWeight > w[templevel] ){
            leftWeight -= w[templevel];
            maxLeft += v[templevel];
            templevel++;
        }
        if( templevel <= n-1){
            maxLeft += v[templevel]/w[templevel]*leftWeight;
        }
        return maxLeft;
    }
}

public class Main {
    public static void main(String[] args) {

        int[] arr = new int[]{3,4,5};
        double[] arr2 = new double[]{4, 5, 6};
        int[] arr3 = new int[]{4, 5, 6};

        queen queen = new queen();
        backpack backpack = new backpack();
        BranchAndBound branchAndBound = new BranchAndBound();


        //N皇后问题 N Queens
        System.out.println(Arrays.deepToString(queen.QueenMain(4)));
        //01背包 动态规划 Dynamic Programing
        System.out.println(Arrays.deepToString(backpack.dynamic(arr, arr3, 10, 3)));
        //01背包 分支限界法 Branch and Bound
        BranchAndBound.setvalue(10,3,arr,arr2);
        BranchAndBound.getMaxValue();
        System.out.println(BranchAndBound.maxValue);

    }
}
