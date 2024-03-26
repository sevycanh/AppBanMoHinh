<?php
include "connect.php";
$accountId = $_POST['accountId'];

$query = "SELECT `product_id`, `quantity` FROM `tbl_cart` WHERE `account_id`= '.$accountId.' ";
$data = mysqli_query($conn, $query);
$result = array();

while ($row = mysqli_fetch_assoc($data)) {
    $result[] = ($row);
}

$result1 = array();
if (!empty($result)){
	foreach($result as $key => $value){
        $quantityProductInCart = $value["quantity"];
		$query1 = "SELECT * FROM `tbl_product` WHERE `product_id`= '".$value["product_id"]."'";
		$data1 = mysqli_query($conn, $query1);
        $row = mysqli_fetch_assoc($data1);
        if($row["quantity"] > $quantityProductInCart){
            $row["quantity"] = $quantityProductInCart;
        }
        $result1[] = ($row);
	}
    if(!empty($result1)){
        $arr =[
            'success' => true,
            'result' => $result1
        ];
        print_r(json_encode($arr));
    }
}
else{
    $arr =[
        'success' => false
    ];
    print_r(json_encode($arr));
}
// 	$arr = [
// 		'success' => true,
// 		'message' => "thanh cong",
// 		'result' => $data
// 	];
// } else {
// 	$arr = [
// 		'success' => false,
// 		'message' => "khong thanh cong",
// 		'result' => $data
// 	];
// }

?>