/* ################################################## */
/* # one thousand genomes sample                                      # */
/* ################################################## */

CREATE TABLE IF NOT EXISTS one_thousand_genomes_sample
(   
    id SERIAL PRIMARY KEY,
    genome_sample JSON
);