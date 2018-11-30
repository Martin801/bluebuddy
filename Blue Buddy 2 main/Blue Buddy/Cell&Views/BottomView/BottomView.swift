//
//  BottomView.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class BottomView: UIView {

    @IBOutlet weak var btn5: UIButton!
    @IBOutlet weak var btn4: UIButton!
    @IBOutlet weak var btn3: UIButton!
    @IBOutlet weak var btn2: UIButton!
    @IBOutlet weak var btn1: UIButton!
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    func designBtmView() {
            self.adjustImageAndTitleOffsetsForButton(button: self.btn1)
            self.adjustImageAndTitleOffsetsForButton(button: btn2)
            self.adjustImageAndTitleOffsetsForButton(button: btn3)
            self.adjustImageAndTitleOffsetsForButton(button: btn4)
            self.adjustImageAndTitleOffsetsForButton(button: btn5)

    }
    private func adjustImageAndTitleOffsetsForButton (button: UIButton) {
        
        let spacing: CGFloat = 6.0
        
        let imageSize = button.imageView!.frame.size
        
        button.titleEdgeInsets = UIEdgeInsetsMake(0, -imageSize.width, -(imageSize.height + spacing), 0)
        
        let titleSize = button.titleLabel!.frame.size
        
        button.imageEdgeInsets = UIEdgeInsetsMake(-(titleSize.height + spacing), 0, 0, -titleSize.width)
    }
}
