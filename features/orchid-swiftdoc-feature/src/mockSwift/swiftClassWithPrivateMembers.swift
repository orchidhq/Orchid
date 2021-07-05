import UIKit

public class SwiftClassWithPrivateMembers {

    public init() {

    }

    private convenience init(dontShowThisConstructor: Int) {
        self.init()
    }
    public convenience init(showThisConstructor: String) {
        self.init()
    }

    private var dontShowThisProperty: Int? = 0
    public var showThisProperty: String? = ""

    internal func dontShowThisMethod() -> Int {
        return 0
    }
    public func showThisMethod() -> String {
        return ""
    }
}
