import UIKit

/**
  Struct description

     - important: Make sure you read this
     - returns: a Llama spotter rating between 0 and 1 as a Float
     - parameter totalLlamas: The number of Llamas spotted on the trip

 More description
 */
class SwiftClass : NSObject {

    /// A String
    var stringVariable: String?

    /// A Number
    var numberVariable: CGFloat?

    /**
     * Initializer Description
     */
    init(dictionary: [String: AnyObject]) {
        self.stringVariable = dictionary["stringVariable"] as! String
        self.numberVariable = dictionary["numberVariable"] as! CGFloat
    }

    /**
     * Test Method Description
     */
    func testMethod(p1: String, p2: CGFloat?) -> String {
        return ""
    }
}
