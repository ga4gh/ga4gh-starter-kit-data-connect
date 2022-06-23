import json
import csv

def tsv_to_json(tsv_file, uuid_file):
	json_arr = []
	uuid_arr = []
	hostname = "localhost:5000"
	wgs_cram_drs_uri = "drs://" + hostname + "/"
	
	uuid = open(uuid_file, 'r')
	for line in uuid:
		uuid_arr.append(line.strip())

	tsv = open(tsv_file, 'r')
	a = tsv.readline()
	n = 0
	titles = [t.strip() for t in a.split('\t')]
	for line in tsv:
		d = {}
		for t, f in zip(titles, line.split('\t')):
			t = t.lower().replace(" ","_")
			d[t] = f.strip()

		d["wgs_cram_drs_uri"] = wgs_cram_drs_uri + uuid_arr[n]
		json_arr.append(d)
		n += 1
	
	return json.dumps(json_arr,indent=4)
	

# Driver Code
input_filename = "igsr_samples.tsv"
uuid_filename = "uuid.csv"
output_filename = "igsr_samples.json"
json_output = tsv_to_json(input_filename, uuid_filename)

with open(output_filename, 'w', encoding='utf-8') as json_file:
	json_file.write(json_output)