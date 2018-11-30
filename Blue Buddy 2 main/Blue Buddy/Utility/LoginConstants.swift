//
//  LoginConstants.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/6/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
class LoginConstants: NSObject {
//    static let sharedInstance: LoginConstants = LoginConstants()
//    private override init() {
//    
//    }
    var objProfile = ProfileBO()
    class var sharedManager: LoginConstants {
        struct Static {
            static let instance = LoginConstants()
        }
        return Static.instance
    }
}
let LOGIN_CONSTANT = LoginConstants.sharedManager
