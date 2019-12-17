import UIKit

/**
  Struct description

     - important: Make sure you read this
     - returns: a Llama spotter rating between 0 and 1 as a Float
     - parameter totalLlamas: The number of Llamas spotted on the trip

 More description
 */
public class SwiftClass : NSObject {

    /// A String
    public var stringVariable: String?

    /// A Number
    public var numberVariable: CGFloat?

    /**
     * Initializer Description
     */
    public init(dictionary: [String: AnyObject]) {
        self.stringVariable = dictionary["stringVariable"] as! String
        self.numberVariable = dictionary["numberVariable"] as! CGFloat
    }

    /**
     * Test Method Description
     */
    public func testMethod(p1: String, p2: CGFloat?) -> String {
        return ""
    }

    /**
    Run this application with the arguments provided

    - Parameters:
        - args: the args to configure the application
     */
    public func start(_ args: String...) {

    }

    /**
    Run this application with the arguments provided

    - Parameters:
        - args: the args to configure the application
    - Returns:
        - a string
     */
    public func start(_ args: String...) -> String {
        return ""
    }
}
