import json

def tsv_to_json(tsv_file):
	arr = []
	file = open(tsv_file, 'r')
	a = file.readline()
	wgs_cram_drs_uri = ""
	
	titles = [t.strip() for t in a.split('\t')]
	for line in file:
		d = {}
		for t, f in zip(titles, line.split('\t')):
			d[t] = f.strip()

		d["DRS URI"] = wgs_cram_drs_uri
		arr.append(d)
		
	return json.dumps(arr,indent=4)
	

# Driver Code
input_filename = 'igsr_samples.tsv'
output_filename = 'igsr_samples.json'
json_output = tsv_to_json(input_filename)

with open(output_filename, 'w', encoding='utf-8') as json_file:
	json_file.write(json_output)