//
//  ImgCollectionViewCell.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 05/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit

class ImgCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var imgProduct: UIImageView!
    @IBOutlet weak var btnDelete: UIButton!
    var onClickDelete: (() -> Void)? = nil
    var onClickAdd: (() -> Void)? = nil
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        if ((onClickAdd) != nil) {
            onClickAdd!()
        }
    }
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        if ((onClickDelete) != nil) {
            onClickDelete!()
        }
    }
}
