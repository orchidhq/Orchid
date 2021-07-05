package com.eden.orchid.azure

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class AzureFlags : OrchidFlag() {

    @Option
    @Protected
    @StringDefault("")
    @Description("Your Azure DevOps Personal Access Token.")
    lateinit var azureToken: String
}
