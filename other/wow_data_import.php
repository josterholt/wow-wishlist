<?php

/**
 * Environment variables & Class/Function definition
 */
$database_name = 'wowdata';
$content_type = 'item';
$collection_name = 'items';

$cache_dir = "data_cache/";

$db = new PDO('mysql:host=ubuntu;port=3306;dbname=wishlist', 'dbuser', 'dbuser');

function microtime_float()
{
	return microtime(true);
}

class BatchManager
{
	private $_startTime = null;
	private $_batchDurationThreshold = 1;
	private $_batchNumItemThreshold = 100;
	private $_numItems = 0;

	function __construct($numItems = 100, $durationThreshold = 1)
	{
		$this->_startTime = microtime(true);

		$this->_batchDurationThreshold = $durationThreshold;
		$this->_batchNumItemThreshold = $numItems;
	}

	private function _getBatchDifference()
	{
		return microtime(true) - $this->_startTime;
	}

	private function _checkStartNewBatch()
	{
		if ($this->_getBatchDifference() > $this->_batchDurationThreshold) {
			echo "Resetting timer...\n";
			$this->_startTime = microtime(true);
			$this->_numItems = 0;
		}
	}

	public function checkBatchWait()
	{
		$this->_checkStartNewBatch();

		if ($this->_numItems > $this->_batchNumItemThreshold && $this->_getBatchDifference() < $this->_batchDurationThreshold) {
			echo "Sleeping... (" . $this->_getBatchDifference() . ")\n";
			usleep(1000000);
		}
	}

	public function incrementAndCheckWait()
	{
		$this->incrementNumItems();
		$this->checkBatchWait();
	}

	public function incrementNumItems()
	{
		$this->_numItems++;
	}
}

/**
 * SCRIPT
 */
echo "Start at: " . date("m-d-Y h:i:s") . "\n";
$SCRIPT_START_TIME = microtime_float(true);
$bm = new BatchManager(100, 1);


$has_content = true;
$start_num = 0;
$end_num   = 127000;

$id = $start_num;

$insertQuery = $db->prepare("INSERT INTO items (name, description, summary, icon, wowId, requiredSkillRank, itemLevel, sellPrice, created, updated) VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW());");
$updateQuery = $db->prepare("UPDATE items SET name = ?, description = ?, icon = ?, requiredSkillRank = ?, itemLevel = ?, sellPrice = ?, updated = NOW() WHERE wowId = ?");
$insertUpdateQuery = $db->prepare("INSERT INTO items (name, description, summary, icon, wowId, requiredSkillRank, itemLevel, sellPrice, created, updated) VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW()) ON DUPLICATE KEY UPDATE name = ?, description = ?, summary = ?, icon = ?, requiredSkillRank = ?, itemLevel = ?, sellPrice = ?, updated = NOW();");

$db->beginTransaction();

$updates = 0;
$transaction_threshold = 20000;

if (!$fp = fopen("./master.json", "r")) {
	die("Unable to open master file\n");
}


while ($has_content) {
	if ($id > $end_num) {
		break;
	}
	$url = "https://us.api.battle.net/wow/{$content_type}/{$id}?apikey=***REMOVED***";
	//echo "### Item {$id} ###\n";

	$json_file = $cache_dir . $id . ".json";

	$cached_file = false;
	/*
	if(!file_exists($json_file)) {
		$content = file_get_contents($url);
		file_put_contents($json_file, $content);		
	} else {
		echo "Using cached file...\n";
		$content = file_get_contents($json_file);
		$cached_file = true;
	}
	*/
	//echo "Using cached file...\n";
	if (!$content = fgets($fp)) {
		$has_content = false;
	}
	//echo $content."\n";
	$cached_file = true;


	$id++;
	if (empty($content) || $content == "\n") {
		//echo "Empty response, skipping\n";
		continue;
	}

	$item = json_decode($content);

	//echo "Searching for existing {$item->id}\n";
	//$existing = $collection->findone(array("id" => $item->id));
	//$query = $db->query("SELECT COUNT(*) > 0 FROM items WHERE wowId = ". (int) $item->id);
	//$query->execute();

	//$existing = $query->fetch()[0];

	/*
	if(!empty($existing)) {
		$updateQuery->execute(array($item->name, $item->description, $item->icon, $item->id, $item->requiredSkillRank, $item->itemLevel, $item->sellPrice));
		echo "Updated\n";		
	} else {
		$insertQuery->execute(array($item->name, $item->description, "", $item->icon, $item->id, $item->requiredSkillRank, $item->itemLevel, $item->sellPrice));
		echo "Add\n";		
	}
	*/
	//echo "Adding query...\n";
	$insertUpdateQuery->execute(array($item->name, $item->description, "", $item->icon, $item->id, $item->requiredSkillRank, $item->itemLevel, $item->sellPrice, $item->name, $item->description, "", $item->icon, $item->requiredSkillRank, $item->itemLevel, $item->sellPrice));

	//echo $updates ." ".$transaction_threshold."\n";
	if ($updates >= $transaction_threshold) {
		//echo "Memory before commit: ".memory_get_usage(true)."\n";
		$db->commit();
		$updates = 0;
		//echo "Memory after commit: ".memory_get_usage(true)."\n";
		$db->beginTransaction();
		//echo "Memory after beginTransaction: ".memory_get_usage(true)."\n";
	}

	$updates += 1;
	if (!$cached_file) {
		$bm->incrementAndCheckWait();
	}
}

if ($updates > 0) {
	$db->commit();
}
fclose($fp);
echo "End script\n";
echo "Time Elapsed: " . (microtime_float(true) - $SCRIPT_START_TIME) . "\n";
echo "Endeded at: " . date("m-d-Y h:i:s") . "\n";
