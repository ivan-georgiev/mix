<?php
require_once('classes/DB.php');

class DBS extends DB{


	/* CONSTRUCTOR */

	public function __construct($serverName, $_connProps) {
		parent::__construct($serverName, $_connProps);
	}

	/* GET METHODS */

	public function getFaculties(){

		$rs = parent::execStatement("select * from FACULTIES");

		$rows = array();

		while( $row = sqlsrv_fetch_array( $rs, SQLSRV_FETCH_BOTH )) {
			$rows[]=$row;
		}

		parent::freeStm($rs);

		return $rows;
	}

	public function getTheses($author, $thTitle, $facN, $anot, $keyw){


		if(empty($author))
		$author='%';
		else
		$author='%'.$author.'%';

		if(empty($thTitle))
		$thTitle='%';
		else
		$thTitle='%'.$thTitle.'%';
		
		if(empty($anot))
		$anot='%';
		else
		$anot='%'.$anot.'%';

		if($facN=="0")
		$facN='%';


		$rs = parent::execStatement("select distinct (t1.ID),
		 t1.ATH_FNAME + ' '+ t1.ATH_LNAME as NAME, t1.TITLE, t1.ANNOTATION,
		  t3.NAME as DEGREE,  t6.ID as FACULTY_ID, T1.DT_OBTAINED as DT_OBTAINED,
		  t2.NAME as SPECIALITY
		from thesises t1
		inner join SPECIALITIES t2 on t1.SPECIALITY_ID = t2.ID
		inner join DEGREES t3 on t2.DEGREE_ID = t3.ID
		left join DOCUMENTS t4 on t1.ID = t4.THESIS_ID
		inner join DEPARTAMENTS t5 on t2.DEPARTAMENT_ID = t5.ID
		inner join FACULTIES t6 on t5.FACULTY_ID = t6.ID
		where t1.DT_OBTAINED IS NOT NULL
		AND t4.PUBLIC_ACCESS=1
		AND (t1.ATH_FNAME like ? OR t1.ATH_LNAME like ?) AND t1.TITLE like ? AND t6.ID like ? AND t1.ANNOTATION like ?
		ORDER BY DT_OBTAINED ",
		$author, $author, $thTitle, $facN, $anot);

		$rows = array();

		while( $row = sqlsrv_fetch_array( $rs, SQLSRV_FETCH_BOTH )) {

			$rows[] = $row;
		}

		parent::freeStm($rs);

		return $rows;
	}

	public function getFiles($thesisId){

		$rs = parent::execStatement("SELECT d1.ID as ID, d1.SIZE, d1.DESCRIPTION ,d1.DT_ADDED, d1.FILE_EXTENSION, d2.NAME as TYPE
		FROM [Thesises].[dbo].[DOCUMENTS] d1
		INNER JOIN dbo.DOCUMENT_TYPES  d2  on d2.[ID] = d1.TYPE_ID
		WHERE d1.THESIS_ID=? and d1.PUBLIC_ACCESS=1", $thesisId);


		$rows = array();
		while( $row = sqlsrv_fetch_array( $rs, SQLSRV_FETCH_BOTH )) {

			$rows[] = $row;
		}

		parent::freeStm($rs);

		return $rows;
	}

	/* FILE METHODS */

	public function getFile($id){

		$rs = parent::execStatement("select SIZE , FILE_EXTENSION , THESIS_ID , BINCONTENT from DOCUMENTS where ID=? and PUBLIC_ACCESS=1", $id);

		$success = sqlsrv_fetch( $rs );

		if($success == false || $success == null)
		{
			echo "No such file!";
			die( print_r( sqlsrv_errors(), true));
		}

		$size=intval(trim(sqlsrv_get_field($rs, 0)));
		$ext=trim(sqlsrv_get_field($rs, 1));
		$name=sqlsrv_get_field($rs, 2).'_'.$id.$ext;
		$content = sqlsrv_get_field( $rs, 3, SQLSRV_PHPTYPE_STREAM(SQLSRV_ENC_BINARY));

		header("Content-Type: ".$this->getMIMEType($ext));
		header("Content-Disposition: attachment; filename=".$name);
		header("Content-Length: ".$size);
		fpassthru($content);

		parent::freeStm($rs);

	}

	private function getMIMEType($ext){

		switch ($ext) {
			case ".pdf":
			return "application/pdf";
			case ".docx":
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			case ".doc":
			return "application/msword";
			case ".xls":
			return "application/vnd.ms-excel";
			case ".xlsx":
			return "application/vnd.ms-excel";
			case ".ppt":
			return "application/vnd.ms-powerpoint";
			case ".pptx":
			return "application/vnd.ms-powerpoint";
			case ".zip":
			return "application/zip";
			default:
			return "application/octet-stream";
		}

	}


	/* EXAMPLES */

	private function selectAllCountries(){

		parent::connect();

		$rs = parent::execStatement("select * from countries");

		while( $row = sqlsrv_fetch_array( $rs, SQLSRV_FETCH_BOTH )) {
			echo $row['ID'].", ".$row['NAME']."<br />";
		}

		parent::freeStm($rs);
		parent::disconnect();

	}

	private function updateCountry($id, $name){

		parent::connect();

		$rs = parent::execStatement("update countries  set NAME=? where ID=?", $name, $id);

		parent::freeStm($rs);
		parent::disconnect();

	}


}
?>