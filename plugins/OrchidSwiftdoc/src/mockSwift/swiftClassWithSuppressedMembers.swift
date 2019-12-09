import UIKit

public class SwiftClassWithSuppressedMembers {

    public init() {

    }

    /**
    - suppress
    */
    public convenience init(dontShowThisConstructor: Int) {
        self.init()
    }

    public convenience init(showThisConstructor: String) {
        self.init()
    }

    /**
    - suppress
    */
    public var dontShowThisProperty: Int? = 0
    public var showThisProperty: String? = ""

    /**
    - suppress
    */
    public func dontShowThisMethod() -> Int {
        return 0
    }
    public func showThisMethod() -> String {
        return ""
    }
}
