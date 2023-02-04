#define ITEM_SIZE 80
#define DP_SIZE (ITEM_SIZE + 1) * (ITEM_SIZE + 1)

__kernel void compute(__global int  *targets, __global int  *query, __global int *results){
  const size_t globalId = get_global_id(0);
  int n = targets[(globalId * (ITEM_SIZE + 1))];
  int x[ITEM_SIZE];
  for (int i = 0; i<n; i++){
     x[i]  = targets[(((globalId * (ITEM_SIZE + 1)) + i) + 1)];
  }
  int m = query[0];
  int y[ITEM_SIZE];
  for (int i = 0; i<m; i++){
     y[i]  = query[(i + 1)];
  }

  int dp[DP_SIZE];
    for (int i = 0; i<=m; i++){
      for (int j = 0; j<=n; j++){
        if (j==0){
           dp[i + (j * n)]  = i;
        } else {
           if (i==0){
              dp[i + (j * n)]  = j;
           } else {
               if (x[(j - 1)]==y[(i - 1)]) {
                   dp[i + (j * n)] = dp[(i - 1) + ((j - 1) * n)];
               } else {
                   dp[i + (j * n)] = 1 + min(dp[(i - 1) + (j * n)], min(dp[ i + (j - 1) * n], dp[(i - 1) +  ((j - 1) * n)]));
               }
           }
        }
     }
  }
 results[globalId]  = dp[((n * n) + m)];
}
