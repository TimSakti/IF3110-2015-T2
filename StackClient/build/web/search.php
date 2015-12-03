<?php
	echo "<p>Hasil Query: <hr></p>";
	$query = $_POST['topicsearch'];
    $sql = "SELECT * FROM question WHERE konten LIKE %".$query."% OR topik LIKE %".$query ."%";;
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        // output data of each row
        while($row = mysqli_fetch_assoc($result)) {
            $ans = mysqli_query($conn,"SELECT count(*) AS answ FROM answer WHERE idquest =" . $row['id']);
            $low = mysqli_fetch_assoc($ans);
            echo "\t\t".$row["vote"]."\t\t". $low['answ'] ."       "."<a href='detail.php?id=".$row['id']."'>".$row["topik"] ."</a>       </p><br>";
            echo "<p>Votes " ."  Answer\t ". " Asked by " . $row["nama"]." ". $row["waktu"]. "| <a href='edit.php?id=".$row['id']."'>edit</a> | <a href='delete.php?id=".$row['id']."'>delete</a> </p><br>";
            echo "<hr>";
        }
    } else {
        echo "no results";
    }
?> 