#define ITEM_SIZE 80
#define DP_SIZE (ITEM_SIZE + 1) * (ITEM_SIZE + 1)

__kernel void compute(__global int  *targets, __global int  *query, __global int *results){
  const size_t globalId = get_global_id(0);
  int xlen = targets[(globalId * (ITEM_SIZE + 1))];
  int x[ITEM_SIZE];
  for (int j = 0; j<xlen; j++){
     x[j]  = targets[(((globalId * (ITEM_SIZE + 1)) + j) + 1)];
  }
  int ylen = query[0];
  int y[ITEM_SIZE];
  for (int j = 0; j<ylen; j++){
     y[j]  = query[(j + 1)];
  }

  int dp[DP_SIZE];
  for (int i = 0; i<=xlen; i++){
     for (int j = 0; j<=ylen; j++){
        if (i==0){
           dp[(i * xlen) + j]  = j;
        } else {
           if (j==0){
              dp[(i * xlen) + j]  = i;
           } else {
              int substitutionCost = (x[(i - 1)]==y[(j - 1)])?0:1;
              dp[(i * xlen) + j]  = min(min((dp[((((i - 1) * xlen) + j) - 1)] + substitutionCost), (dp[(((i - 1) * xlen) + j)] + 1)), (dp[(((i * xlen) + j) - 1)] + 1));
           }
        }
     }
  }
 results[globalId]  = dp[((xlen * xlen) + ylen)];
}