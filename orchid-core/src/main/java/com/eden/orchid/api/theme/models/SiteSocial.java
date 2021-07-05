package com.eden.orchid.api.theme.models;

import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;

/**
 * This is the description of the class.
 */
// the following archetpes are deprecated, and are used for a smooth transition from setting social info in the theme,
// to being in the SiteInfo
@Archetype(value = ConfigArchetype.class, key = "theme.social")
@Archetype(value = ConfigArchetype.class, key = "BsDoc.social")
@Archetype(value = ConfigArchetype.class, key = "Editorial.social")
@Archetype(value = ConfigArchetype.class, key = "FutureImperfect.social")
@Archetype(value = ConfigArchetype.class, key = "Copper.social")
public class SiteSocial extends Social {

}
