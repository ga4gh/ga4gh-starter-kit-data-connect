import json

def tsv_to_json(tsv_file):
    json_arr = []
    
    drs_uri_template_start = "drs://localhost:5000/"
    drs_uri_template_suffix = ".1kgenomes.wgs.downsampled."

    tsv = open(tsv_file, 'r')
    header = tsv.readline()
    n = 0
    titles = [t.strip() for t in header.split('\t')]
    for line in tsv:
        if n < 200:
            sample = {}
            for field_name, field_value in zip(titles, line.strip().split('\t')):
                field_name = field_name.lower().replace(" ","_")
                sample[field_name] = field_value
            
            drs_uri_template = drs_uri_template_start + sample["sample_name"] + drs_uri_template_suffix
            sample["cram_drs_uri"] = drs_uri_template + "cram"
            sample["crai_drs_uri"] = drs_uri_template + "crai"
            sample["bundle_drs_uri"] = drs_uri_template + "bundle"

            json_arr.append(sample)
        n += 1

    return json_arr

def main():
    # Driver Code
    input_filename = "igsr_samples.tsv"
    output_filename = "igsr_samples.json"
    samples_array = tsv_to_json(input_filename)

    # output as "pretty" JSON array
    with open(output_filename, 'w', encoding='utf-8') as json_file:
        json_file.write(json.dumps(samples_array,indent=4))
    
    # output as .ndjson, one JSON record per line
    ndjson_filename = "igsr_samples.ndjson"
    ndjson_file = open(ndjson_filename, "w")
    ndjson_file.write("")
    ndjson_file.close()
    ndjson_file = open(ndjson_filename, "a")
    for sample in samples_array:
        ndjson_file.write(json.dumps(sample) + "\n")
    ndjson_file.close()

if __name__ == "__main__":
    main()