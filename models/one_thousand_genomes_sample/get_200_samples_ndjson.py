import json

def create_200_sample_dictionary(tsv_file, sample_ids_of_interest):
    sample_dict = {}
    
    drs_uri_template_start = "drs://localhost:5000/"
    drs_uri_template_suffix = ".1kgenomes.wgs.downsampled."

    tsv = open(tsv_file, 'r')
    header = tsv.readline()
    titles = [t.strip() for t in header.split('\t')]
    for line in tsv:
        sample = {}
        for field_name, field_value in zip(titles, line.strip().split('\t')):
            field_name = field_name.lower().replace(" ","_")
            sample[field_name] = field_value
        
        # only add the sample data if it's in the list of 200 sample ids
        if sample["sample_name"] in sample_ids_of_interest:
        
            drs_uri_template = drs_uri_template_start + sample["sample_name"] + drs_uri_template_suffix
            sample["cram_drs_uri"] = drs_uri_template + "cram"
            sample["crai_drs_uri"] = drs_uri_template + "crai"
            sample["bundle_drs_uri"] = drs_uri_template + "bundle"

            sample_dict[sample["sample_name"]] = sample

    return sample_dict

def create_200_sample_array(sample_dict, sample_ids_of_interest):
    return [sample_dict[sample] for sample in sample_ids_of_interest]

def main():
    
    # 200 samples we will include in the final output
    sample_ids_of_interest = open("sample_ids.txt", "r").read().split("\n")[:-1]
    input_filename = "igsr_samples.tsv"
    output_filename = "igsr_samples.json"

    # create an array of sample data in the same order as input sample ids
    sample_dict_200 = create_200_sample_dictionary(input_filename, set(sample_ids_of_interest))
    sample_array_200 = create_200_sample_array(sample_dict_200, sample_ids_of_interest)
    
    # output as .ndjson, one JSON record per line
    ndjson_filename = "igsr_samples.ndjson"
    ndjson_file = open(ndjson_filename, "w")
    ndjson_file.write("")
    ndjson_file.close()
    ndjson_file = open(ndjson_filename, "a")
    for sample in sample_array_200:
        ndjson_file.write(json.dumps(sample) + "\n")
    ndjson_file.close()

if __name__ == "__main__":
    main()