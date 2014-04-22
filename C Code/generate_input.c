#include <stdio.h>
#include <stdlib.h>
#include <sys/times.h>

#include "basket.h"

int main(int argc, char **argv)
{
    int i, j, max_id=0;
     /* print usage if needed */
    if (argc != 2) {
        fprintf(stderr, "Usage: %s total_basket_number\n", argv[0]);
        exit(0);
    }
    
    /* get total record number */ 
    int total_basket_number = atoi(argv[1]);
    
    char filename[1024];
    FILE *fp = NULL;
    FILE *input = fopen("transa.txt", "w");

    struct timeval time_start, time_end;

    /* start time */
    gettimeofday(&time_start, NULL);

    for (i = 0; i < total_basket_number; i++) {
        /* open the corresponding file */
        sprintf(filename, "../data/basket_%06d.dat", i);

        fp = fopen(filename,"rb");

        if (!fp) {
            fprintf(stderr, "Cannot open %s\n", filename);
            exit(0);
        }

        /* read the record from the file */
        basket_t *bp = read_basket(fp);

	/* =========== start of data processing code ================ */
        for (j=0; j < bp->item_num; j++)
        {
            if(bp->items[j].item_id > max_id)
            {
	        max_id = bp->items[j].item_id;
            }
            fprintf(input, "%i", bp->items[j].item_id);
            fprintf(input, " ");
        }
	fprintf(input, "\n");

        /* =========== end of data processing code ================ */

        /* free memory */
        free_basket(bp);

        /* close the file */
        fclose(fp);
    }
    
    fclose(input);

    /* end time */
    gettimeofday(&time_end, NULL);

    float totaltime = (time_end.tv_sec - time_start.tv_sec)
                    + (time_end.tv_usec - time_start.tv_usec) / 1000000.0f;

    printf("\n\nProcess time %f seconds\n", totaltime);

    return 0;
}
