<?php

$dir = "./data_cache/";

if(!$mh = fopen("./master.json", "w")) {
	die("Unable to open file for writing\n");
}

for($i=0; $i < 127000; $i++) {
	$filename = "{$i}.json";
	echo $filename."\n";
	
	$contents = file_get_contents($dir.$filename);
	$contents .= "\n";
	
	fwrite($mh, $contents, strlen($contents));
}
fclose($mh);