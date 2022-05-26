import json

def main():
    input_file = "igsr_samples.ndjson"
    samples = []
    for line in open(input_file, "r"):
        sample = json.loads(line.strip())
        samples.append(sample)
    
    attributes = [
        "sex",
        "population_code",
        "population_name",
        "superpopulation_code",
        "superpopulation_name"
    ]
    
    for attribute in attributes:
        unique_records = set([sample[attribute] for sample in samples])
        print("%s unique records in %s" % (str(len(unique_records)), attribute))
        print(unique_records)
        print("")
    
    



if __name__ == "__main__":
    main()
