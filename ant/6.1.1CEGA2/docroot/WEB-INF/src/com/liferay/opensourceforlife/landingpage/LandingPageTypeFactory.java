/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.HashMap;
import java.util.Map;

import com.liferay.opensourceforlife.util.CustomLandingPageConstant;

/**
 * @author tejas.kanani
 */
public final class LandingPageTypeFactory {

	private LandingPageTypeFactory() {

		super();
	}

	private static Map<String, LandingPageType> pageTypeMap = new HashMap<String, LandingPageType>();
	static {
		pageTypeMap.put(CustomLandingPageConstant.USER_PRIVATE_PAGE,
				new UserPrivatePageType());
		pageTypeMap.put(CustomLandingPageConstant.USER_PUBLIC_PAGE,
				new UserPublicPageType());
		pageTypeMap.put(CustomLandingPageConstant.SITE_PUBLIC_PAGE,
				new SitePublicPageType());
		pageTypeMap.put(CustomLandingPageConstant.SITE_PRIVATE_PAGE,
				new SitePrivatePageType());
		pageTypeMap.put(CustomLandingPageConstant.ORGANIZATION_PUBLIC_PAGE,
				new OrganizationPublicPageType());
		pageTypeMap.put(CustomLandingPageConstant.ORGANIZATION_PRIVATE_PAGE,
				new OrganizationPrivatePageType());
	}

	public static LandingPageType getLandingPageTypeInstance(
			final String landingPageType) {

		return pageTypeMap.containsKey(landingPageType) ? pageTypeMap
				.get(landingPageType) : new DefaultLandingPageType();
	}
}
