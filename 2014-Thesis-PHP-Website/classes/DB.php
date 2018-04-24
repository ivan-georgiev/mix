<?php
class DB{

	private $_serverName;
	private $_conProperties;
	private $_conn;

	protected function __construct($serverName, $conProperties) {

		$this->_serverName=$serverName;
		$this->_conProperties=$conProperties;
	}

	public function connect(){

		$conn = sqlsrv_connect( $this->_serverName, $this->_conProperties);

		if( $conn === false )
		{
			echo "Could not connect.\n";
			die( print_r( sqlsrv_errors(), true));
		}

		$this->_conn= $conn;
	}

	public function disconnect(){
		sqlsrv_close( $this->_conn);
	}

	protected function freeStm( $stmt){
		sqlsrv_free_stmt( $stmt);
	}

	protected function execStatement(){

		$argsCnt = (int)func_num_args();

		if ( $argsCnt == 0)
		throw new Exception('No parameters passed');

		//first parameter is the query
		$query = func_get_arg(0);

		//bind the other parameters
		$params = array();
		for($i = 1 ; $i < $argsCnt; $i++) {
			$params[] = func_get_arg($i);
		}

		/* Execute the query. */
		$stmt = sqlsrv_query($this->_conn, $query, $params);

		if( $stmt === false )
		{
			echo "Error in statement execution.\n";
			die( print_r( sqlsrv_errors(), true));
		}

		return $stmt;
	}
}
?>