<br/>
<FORM ACTION="" METHOD="get" NAME="searchform" style="width:1300px;" >

	<input type="hidden" name="tab" value="2">
	<fieldset>
		<legend>Форма за търсене</legend>
		<table>
			<tr>
				<td>Автор: </td>
				<td>
					<input type="text" name="author" style="width:160px;">
				</td>
				<td>&nbsp;Тема: </td>
				<td>
					<input type="text" name="thTitle" style="width:180px;">
				</td>
				<td>&nbsp;Факултет: </td>
				<td>
					<select name="facCombo" style="width:160px;">
						<?php
						$cnt->drawFaculties();
						?>
					</select>
				</td>
				<td>
					<INPUT type="submit" name="submit" VALUE="Търси" />
				</td>
				<td>
					<INPUT type="reset" VALUE="Изчисти"/>
				</td>
			</tr>
			<tr>
				<td>Анотация: </td>
				<td colspan = "3">
					<input type="text" name="annotation" style="width:397px;">
				</td>
				<td>&nbsp;Ключови думи: </td>
				<td>
					<input type="text" name="keywords" style="width:153px;">
				</td>
			</tr>
		</table>
	</fieldset>

</FORM>
<br/>

<table><tr><td>
	<?php

	if (isset($_GET['submit'])){

		if (isset($_GET['author']))
		$author=$_GET['author'];
		else
		$author="";

		if (isset($_GET['thTitle']))
		$thTitle=$_GET['thTitle'];
		else
		$thTitle="";

		if (isset($_GET['facCombo']))
		$facCombo=$_GET['facCombo'];
		else
		$facCombo=0;
		
			if (isset($_GET['annotation']))
		$annotation=$_GET['annotation'];
		else
		$annotation="";
		
			if (isset($_GET['keywords']))
		$keywords=$_GET['keywords'];
		else
		$keywords="";

		echo '<SCRIPT type="text/javascript" >
		document.searchform.author.value="'.$author.'";
		document.searchform.thTitle.value="'.$thTitle.'";
		document.searchform.facCombo.value="'.$facCombo.'";
		document.searchform.annotation.value="'.$annotation.'";
		document.searchform.keywords.value="'.$keywords.'";
		</SCRIPT>
		';

		$cnt->drawTheses($author, $thTitle, $facCombo, $annotation, $keywords, 'class="tlist"', 'class="outer"');
	}
	?>
</td></tr></table>
