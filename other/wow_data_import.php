<?php
/**
 * Environment variables
 */
$database_name = 'wowdata';
$content_type = 'item';
$collection_name = 'items';

$cache_dir = "data_cache/";

$db = new PDO('mysql:host=ubuntu;port=3306;dbname=wishlist', 'dbuser', 'dbuser');

function microtime_float() {
	//list($usec, $sec) = explode(" ", microtime());	
	//return ((float)$usec + (float)$sec);
	return microtime(true);
}

function microtime_used($before,$after) {
    return (substr($after,11)-substr($before,11)) 
    	+(substr($after,0,9)-substr($before,0,9));
}

function getBatchTime($start_time) {
	return $end_time - $start_time;
}

class BatchManager {
	private $_startTime = null;
	private $_batchDurationThreshold = 1;
	private $_batchNumItemThreshold = 100;
	private $_numItems = 0;

	function __construct($numItems = 100, $durationThreshold = 1) {
		$this->_startTime = microtime(true);

		$this->_batchDurationThreshold = $durationThreshold;
		$this->_batchNumItemThreshold = $numItems;
	}

	private function _getBatchDifference() {
		return microtime(true) - $this->_startTime;
	}

	private function _checkStartNewBatch() {
		if($this->_getBatchDifference() > $this->_batchDurationThreshold) {
			echo "Resetting timer...\n";
			$this->_startTime = microtime(true);
			$this->_numItems = 0;
		}
	}

	public function checkBatchWait() {
		$this->_checkStartNewBatch();		

		if($this->_numItems > $this->_batchNumItemThreshold && $this->_getBatchDifference() < $this->_batchDurationThreshold) {
			echo "Sleeping... (".$this->_getBatchDifference().")\n";
			usleep(1000000);
		}
	}

	public function incrementAndCheckWait() {
		$this->incrementNumItems();
		$this->checkBatchWait();
	}

	public function incrementNumItems() {
		$this->_numItems++;
	}
}
$bm = new BatchManager(100, 1);

/*
$i = 0;
while($i < 1000) {
	$bm->incrementAndCheckWait();
	//usleep(1000000);
	echo $i."\n";
	$i++;
}
die();
*/

$has_content = true;
$start_num = 0;
$end_num   = 127000;
//$start_num = 1998;
//$end_num = 1998;

$id = $start_num;

$insertQuery = $db->prepare("INSERT INTO items (name, description, summary, icon, wowId) VALUES(?, ?, ?, ?, ?);");
$updateQuery = $db->prepare("UPDATE items SET name = ?, description = ?, icon = ? WHERE wowId = ?");

while($has_content) {
	if($id > $end_num) {
		break;
	}
	$url = "https://us.api.battle.net/wow/{$content_type}/{$id}?apikey=***REMOVED***";
	echo "### Item {$id} ###\n";

	$json_file = $cache_dir.$id.".json";

	if(!file_exists($json_file)) {
		$content = file_get_contents($url);
		file_put_contents($json_file, $content);		
	} else {
		echo "Using cached file...\n";
		$content = file_get_contents($json_file);
	}

	$id++;
	if(empty($content)) {
		echo "Empty response, skipping\n";
		continue;
	}


	$item = json_decode($content);

	echo "Searching for existing {$item->id}\n";
	//$existing = $collection->findone(array("id" => $item->id));
	$query = $db->query("SELECT COUNT(*) > 0 FROM items WHERE wowId = ". (int) $item->id);
	$query->execute();

	$existing = $query->fetch()[0];

	if(!empty($existing)) {
		$updateQuery->execute(array($item->name, $item->description, $item->icon, $item->id));
		echo "Updated\n";		
	} else {
		//$collection->insert($item);	
		$insertQuery->execute(array($item->name, $item->description, "", $item->icon, $item->id));
		echo "Added\n";		
	}

	$bm->incrementAndCheckWait();		
}
echo "End script\n";
