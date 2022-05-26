CREATE TABLE IF NOT EXISTS phenopacket_v1
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    json_data JSON
);

CREATE TABLE IF NOT EXISTS one_thousand_genomes_sample
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    json_data JSON
);
