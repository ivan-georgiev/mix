<?php

class Controller{

	private $db;

	public function __construct($db) {
		$this->db=$db;
		$this->db->connect();
	}

	public function drawFaculties(){

		$arr = $this->db->getFaculties();

		echo "<option value=\"0\">===";

		foreach( $arr as $row ) {

			$id = $row["ID"];
			$name = $row["NAME"] ;

			echo "<option value=\"$id\">$name";

		}
	}

	public function drawTheses($author, $thTitle, $facN, $anot, $keyw, $cssListProps, $cssFileTProps){

		$arr = $this->db->getTheses($author, $thTitle, $facN, $anot, $keyw);
   

		foreach( $arr as $row ) {

			echo '<br/><div '.$cssListProps.'> Тема:     </div>'.$row["TITLE"];
			echo '<br/><div '.$cssListProps.'> Анотация: </div>'.trim($row["ANNOTATION"]);
			echo '<br/><div '.$cssListProps.'> Автор:    </div>'.$row["NAME"];
			echo '<br/><div '.$cssListProps.'> ОКС:      </div>'.$row["DEGREE"];
			echo '<br/><div '.$cssListProps.'> Специалност:      </div>'.$row["SPECIALITY"];
			echo '<br/><div '.$cssListProps.'> Година:      </div>'.$row["DT_OBTAINED"]->format('Y');
			echo '<br/>';
			echo '<br/><div '.$cssListProps.'> Файлове:  </div>';

			$this->drawFiles($row["ID"], $cssFileTProps);

			echo '<hr/>';

		}
	}

	public function drawFiles($thesisId, $cssFileTProps){

		echo '<table '.$cssFileTProps.'>';
		echo '<th> N </th>';
		echo '<th> Изтегли </th>';
		echo '<th> Тип </th>';
		echo '<th> Разширение </th>';
		echo '<th> Размер </th>';
		echo '<th> Дата </th>';
		echo '<th> Описание </th>';

		$arr = $this->db->getFiles($thesisId);
		$i=1;

		foreach( $arr as $row ) {

			$size = intval(intval($row["SIZE"]) / 1024);
			echo '<tr>';
			echo '<td>'.$i.'</td> ';
			echo '<td> <a target="_blank" href="files.php?fileId='.$row["ID"].'">Изтегли</a></td> ';
			echo '<td>'.$row["TYPE"].'</td> ';
			echo '<td>'.$row["FILE_EXTENSION"].'</td> ';
			echo '<td>'.$size.' KBytes </td> ';
			echo '<td>'.$row["DT_ADDED"]->format('Y-m-d').'</td> ';
			echo '<td>'.$row["DESCRIPTION"].'</td> ';
			echo '</tr>';
			$i=$i+1;
		}

		if($i==1)
		echo '<tr><td colspan="7">No Public Files</td></tr>';

		echo '</table>';

	}
	

}
?> 