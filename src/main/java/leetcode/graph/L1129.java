package leetcode.graph;

import java.util.*;

public class L1129 {

    public static void main(String[] args) {
//        Integer a = new Integer(3000);
//        System.out.println(a.equals(3000));
//        System.out.println();

        int[][] redEdges = {{0,1},{1,2}};
        int[][] blueEdges = {{1,2}};
        int n = 3;
        System.out.println(Arrays.toString(shortestAlternatingPaths(n,redEdges,blueEdges)));
    }

    public static int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // 广度优先遍历
        // 首先把图做一个转换 0.表示红边 1.表示蓝边
        List<Integer>[][] graph = new ArrayList[2][n];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = new ArrayList<Integer>();
            }
        }
        for (int[] redEdge : redEdges) {
            graph[0][redEdge[0]].add(redEdge[1]);
        }
        for (int[] blueEdge : blueEdges) {
            graph[1][blueEdge[0]].add(blueEdge[1]);
        }
        int[][] dist = new int[2][n];
        for (int i = 0; i < 2; i++) {
            Arrays.fill(dist[i],Integer.MAX_VALUE);
        }
        dist[0][0] = 0;
        dist[1][0] = 0;
        Queue<int[]> queue = new LinkedList<int[]>();
        queue.offer(new int[]{0, 0});
        queue.offer(new int[]{1, 0});
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();
            int x = poll[0];    // 判断类型
            int y = poll[1];    // 判断初始点
            // 1 - x 表示为另外的点
            for (int to : graph[1 - x][y]) {
                // 判断当前点是否到达
                if (dist[1 - x][to] != Integer.MAX_VALUE) {
                    continue;
                }
                dist[1 - x][to] = dist[x][y] + 1;
                queue.add(new int[]{1 - x, to});
            }
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = Math.min(dist[0][i], dist[1][i]);
            if (res[i] == Integer.MAX_VALUE) {
                res[i] = -1;
            }
        }
        return res;
    }


    public static int[] shortestAlternatingPath1s(int n, int[][] redEdges, int[][] blueEdges) {
        // 广度优先遍历
        int[] res = new int[n];
        Arrays.fill(res, -1);
        //
        res[0] = 0;
        bfs(redEdges,blueEdges,res);
        bfs(blueEdges,redEdges,res);
        return res;
    }

    public static void bfs(int[][] redEdges, int[][] blueEdges,int[] res) {
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(0);
        // 用来判断 点是否达到
        Set<Integer> set = new HashSet<Integer>();
        int deep = 1;
        while (!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0;i < size;i++){
                // 红 先 黑 后
                Integer poll = queue.poll();
                if ((deep & 1) == 1) {
                    for (int[] redEdge : redEdges) {
                        toAddRes(redEdge,set,deep,res,poll,queue);
                    }
                } else {
                    for (int[] blueEdge : blueEdges) {
                        toAddRes(blueEdge,set,deep,res,poll,queue);
                    }

                }
            }
            deep++;
        }
    }

    public static void toAddRes(int[] edge,Set<Integer> set,int deep,int[] res,int target,Queue<Integer> queue) {
        if (edge[0] == target && set.add(edge[1])) {
            queue.add(edge[1]);
            if (res[edge[1]] == -1) {
                res[edge[1]] = deep;
            } else {
                res[edge[1]] = Math.min(deep, res[edge[1]]);
            }

        }
    }
}
