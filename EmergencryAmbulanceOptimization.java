package EAROP_PROJECT;

import java.util.*;

public class EmergencryAmbulanceOptimization 
{
    // Travel Cost Matrix ( Adjancency Matrix ) 
    static int[][] costMatrix = {
        {0, 15, 25, 35},
        {15, 0, 30, 28},
        {25, 30, 0, 20},
        {35, 28, 20, 0}
    };
    
    //Locations Name 
    static String[] locations = {
        "Hospital",
        "Emergency Location B",
        "Emergency Location C",
        "Emergency Location D"
    };
    
    public static String greedyEAROP(int[][] dist)
    {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        
        int current = 0;
        int totalcost = 0;
        
        StringBuilder route = new StringBuilder();
        
        visited[0] = true;
        route.append(locations[0]);
        
        for (int count = 1; count < n; count++)
        {
            int nearest = -1;
            int minimumCost = Integer.MAX_VALUE;
            
            for ( int i = 0; i < n; i++)
            {
                if (!visited[i] && dist[current][i] < minimumCost)
                {
                    minimumCost = dist[current][i];
                    nearest = i;
                }
            }
            
            visited[nearest] = true;
            totalcost += minimumCost;
            current = nearest;
            
            route.append("->").append(locations[current]);
        }
         //return to hospital 
         totalcost += dist[current][0];
         route.append("->").append(locations[0]);
         
         return "Greedy Ambulance Route: "
                + route 
                + " | total cost : "
                + totalcost;
                // close greedyEAROP 
        }
        // dynamic programming 
        public static String dynamicProgrammingEAROP(int[][] dist)
        {
            int n = dist.length;
            int VISITED_ALL = (1 << n) - 1;
            
            int[][] memo = new int[n][1 << n];
            String[][] paths = new String[n][1 << n];
            
            for (int[] row : memo )
            {
                Arrays.fill(row, -1);
            }
            
            int minCost = dynamicProgrammingEAROPHelper(
                0, 1, dist, memo, VISITED_ALL, paths
                );
                
                String route = locations[0] + paths[0][1];
                
                return "dynamic programming ambulance route: " 
                       + route 
                       + " | totalcost: "
                       + minCost; 
                
        }
        //Dynamic Programming Helper Method 
        private static int dynamicProgrammingEAROPHelper(
        int pos,
        int mask,
        int[][]dist,
        int[][] memo,
        int VISITED_ALL,
        String[][]paths)
        
        {
            //All locations have been visited 
            if (mask == VISITED_ALL)
            {
                paths[pos][mask]= "->" + locations[0];
                return dist[pos][0];
            }
            //return stored result
            if (memo[pos][mask] != -1)
            {
                return memo[pos][mask];
            }
            
            int minimumCost = Integer.MAX_VALUE;
            String bestPath = "";
            
            for (int city = 0; city < dist.length; city++)
            {
                //check whether locations has not been visited
                if((mask & (1 << city)) == 0)
                {
                    int newCost = dist[pos][city]
                        + dynamicProgrammingEAROPHelper(
                          city,
                          mask | (1 << city),
                          dist,
                          memo,
                          VISITED_ALL,
                          paths
                        );
                        
                        if (newCost < minimumCost) 
                        {
                            minimumCost = newCost;
                            bestPath = " -> " + locations[city]
                                + paths[city][mask | (1 << city)];          
                }
            }
        }
        
        memo[pos][mask] = minimumCost;
        paths[pos][mask] = bestPath;
        
        return minimumCost; 
    }
        
    //Backtracking Route Optimization 
    static int bestCost;
    static String bestRoute;
    
    public static String backtrackingEAROP(int[][]dist)
    {
        int n = dist.length;
        
        boolean[]visited = new boolean[n];
        visited[0] = true;
        
        bestCost = Integer.MAX_VALUE;
        bestRoute = "";
        
        List<Integer> path = new ArrayList<>();
        path.add(0);
        
        backtrackHelper(
            0,
            visited,
            1,
            0,
            path,
            dist
        );
        
        return "Backtracking Ambulance Route: "
               + bestRoute
               + " | Total Cost : "
               + bestCost;
    }
    
    private static void backtrackHelper(
        int current,
        boolean[] visited,
        int count,
        int cost,
        List<Integer> path,
        int[][]dist )
    {
        //All emergency locations have been visited 
        if ( count == dist.length)
        {
            int totalCost = cost + dist[current][0];
            
            if(totalCost < bestCost)
            {
                bestCost = totalCost ;
                
                StringBuilder route = new StringBuilder();
                
                for(int location : path)
                {
                    if(route.length() > 0)
                    {
                        route.append(" -> ");
                    }
                    
                    route.append(locations[location]);
                }
                
                route.append(" -> ").append(locations[0]);
                bestRoute = route.toString();
            }
            
            return; 
        }
        
        // try every unvisited location 
        for (int next = 0; next < dist.length; next++)
        {
            if(!visited[next])
            {
                visited[next] = true;
                path.add(next);
                
                backtrackHelper(
                    next,
                    visited,
                    count + 1,
                    cost + dist[current][next],
                    path,
                    dist
                    );
                    
                    //backtrack
                    visited[next] = false;
                    path.remove(path.size() - 1);
            }
        }
    }
    
    //Divide and Conquer Route Optimization
    
    static int divideBestCost;
    static String divideBestRoute;
    
    public static String divideAndConquerEAROP(int[][] dist)
    {
        boolean[] visited = new boolean[dist.length];
        visited[0] = true;
        
        divideBestCost = Integer.MAX_VALUE;
        divideBestRoute = "";
        
        List<Integer> path = new ArrayList<>();
        path.add(0);
        
        divideAndConquerHelper(
            0,
            visited,
            1,
            0,
            path,
            dist
            );
        
        return "Divide and Conquer Ambulance Route: "
               + divideBestRoute
               + " | Total Cost :"
               + divideBestCost;
            
        }
        
    private static void divideAndConquerHelper(
                int current,
                boolean[] visited,
                int count,
                int cost,
                List<Integer> path,
                int[][] dist
                )
         {       
        //base case : all locations have been visited
        if(count == dist.length)
        {
            int totalCost = cost + dist[current][0];
            
            if(totalCost < divideBestCost)
            {
                divideBestCost = totalCost;
                
                StringBuilder route = new StringBuilder();
                
                for(int location : path)
                {
                    if(route.length() > 0)
                    {
                        route.append(" -> ");
                    }
                    
                    route.append(locations[location]);
                }
                
                route.append(" -> ").append(locations[0]);
                divideBestRoute = route.toString();
            }
            
            return;
        }
        
        //divide into smaller route choice
        for(int next = 0; next < dist.length; next++)
        {
            if(!visited[next])
            {
                visited[next] = true;
                path.add(next);
                
                divideAndConquerHelper(
                    next,
                    visited,
                    count + 1,
                    cost + dist[current][next],
                    path,
                    dist
                    );
                    
                    visited[next] = false;
             path.remove(path.size() -1);
            }
            
        }
    }
    
    //Insertion Sort 
    
    public static String insertionSort(int[]arr)
    {
        for (int i = 1; i < arr.length; i++)
        {
            int key = arr[i];
            int j = i - 1;
            
            while (j >= 0 && arr[j] > key)
            {
                arr[j + 1] = arr[j];
                j--;
            }
            
            arr[j + 1] = key;
        }
        
        return Arrays.toString(arr);
    }
        public static int binarySearch(int[] arr, int target)
    {
    int left = 0;
    int right = arr.length - 1;

    while (left <= right)
    {
        int mid = (left + right) / 2;

        if (arr[mid] == target)
        {
            return mid;
        }
        else if (arr[mid] < target)
        {
            left = mid + 1;
        }
        else
        {
            right = mid - 1;
        }
    }

    return -1;
    }
    public static void main(String[] args)
    {
       System.out.println(greedyEAROP(costMatrix)); 
       System.out.println(dynamicProgrammingEAROP(costMatrix));
       System.out.println(backtrackingEAROP(costMatrix));
       System.out.println(divideAndConquerEAROP(costMatrix));
       
       int[] arr ={8, 3, 5, 1, 9, 2};
       
       insertionSort(arr);
       
       System.out.println(
        "Sorted Emergency Response Times: " 
        + Arrays.toString(arr)
        );
        
        int target = 5;
        
        int result = binarySearch(arr, target);
        
        System.out.println(
               "Binary Search: Response time "
               + target
               + " found at index "
               + result
       );
    
    }
}


