/**
 * 
 */

package com.liferay.opensourceforlife.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public final class CustomLandingPageUtil
{

	private CustomLandingPageUtil()
	{
	}

	/**
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getLanguage(final HttpServletRequest request) throws PortalException,
			SystemException
	{

		String language = StringPool.BLANK;
		Locale currentLocale = PortalUtil.getUser(request).getLocale();

		if (GetterUtil.getInteger(PropsUtil.get(PropsKeys.LOCALE_PREPEND_FRIENDLY_URL_STYLE)) == 1
				&& !currentLocale.equals(LocaleUtil.getDefault())
				|| GetterUtil
						.getInteger(PropsUtil.get(PropsKeys.LOCALE_PREPEND_FRIENDLY_URL_STYLE)) == 2)
		{

			language = buildI18NPath(currentLocale);
		}

		return language;
	}

	/**
	 * @param locale
	 * @return
	 */
	private static String buildI18NPath(final Locale locale)
	{

		String languageId = LocaleUtil.toLanguageId(locale);

		if (Validator.isNull(languageId))
		{
			return null;
		}

		if (LanguageUtil.isDuplicateLanguageCode(locale.getLanguage()))
		{
			Locale priorityLocale = LanguageUtil.getLocale(locale.getLanguage());

			if (locale.equals(priorityLocale))
			{
				languageId = locale.getLanguage();
			}
		} else
		{
			languageId = locale.getLanguage();
		}

		return StringPool.SLASH.concat(languageId);
	}

	/**
	 * Generate displayURL for specified group
	 * 
	 * @param request
	 * @param isPrivateLayout
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getDisplayURL(final HttpServletRequest request,
			final boolean isPrivateLayout) throws PortalException, SystemException
	{

		String displayURL = StringPool.BLANK;

		Group group = GroupLocalServiceUtil.getUserGroup(PortalUtil.getCompanyId(request),
				PortalUtil.getUserId(request));

		boolean isLayoutCountEmpty = true;

		if (isPrivateLayout && group.getPrivateLayoutsPageCount() > 0)
		{
			isLayoutCountEmpty = false;
		} else if (!isPrivateLayout && group.getPublicLayoutsPageCount() > 0)
		{
			isLayoutCountEmpty = false;
		}

		if (!isLayoutCountEmpty)
		{
			displayURL = generateLayoutDisplayUrl(group, isPrivateLayout);
		} else
		{
			displayURL = generateUserLayoutDisplayUrl(group, isPrivateLayout);
		}

		return displayURL;
	}

	/**
	 * Generate layout DisplayURL for User's public/private page
	 * 
	 * @param group
	 * @param isPrivateLayout
	 * @return
	 */
	private static String generateLayoutDisplayUrl(final Group group, final boolean isPrivateLayout)
	{
		StringBundler sb = new StringBundler(CustomLandingPageConstant.FIVE);

		sb.append(PortalUtil.getPathMain());
		sb.append("/my_sites/view?groupId=");
		sb.append(group.getGroupId());
		if (isPrivateLayout)
		{
			sb.append("&privateLayout=1");
		} else
		{
			sb.append("&privateLayout=0");
		}

		return sb.toString();
	}

	/**
	 * @param group
	 * @param isPrivateLayout
	 * @return
	 */
	private static String generateUserLayoutDisplayUrl(final Group group,
			final boolean isPrivateLayout)
	{
		StringBundler sb = new StringBundler(CustomLandingPageConstant.FIVE);
		if (isPrivateLayout)
		{
			sb.append(PortalUtil.getPathFriendlyURLPrivateUser());
		} else
		{
			sb.append(PortalUtil.getPathFriendlyURLPublic());
		}

		sb.append(group.getFriendlyURL());

		return sb.toString();
	}

	/**
	 * @param userId
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static List<Group> getSites(final long userId) throws PortalException, SystemException
	{

		List<Group> sites = new ArrayList<Group>();

		for (Group group : GroupLocalServiceUtil.getUserGroups(userId))
		{
			if (group.isRegularSite()
					&& !CustomLandingPageConstant.GUEST_GROUP_FRIENDLY_URL.equalsIgnoreCase(group
							.getFriendlyURL()))
			{
				sites.add(group);
				break;
			}
		}
		return sites;
	}

	/**
	 * @param landingPageValue
	 * @param groupId
	 * @param isPrivateLayout
	 * @return
	 */
	public static String getLayoutFriendlyURL(final String landingPageValue, final long groupId,
			final boolean isPrivateLayout)
	{
		String friendlyURL = StringPool.BLANK;
		try
		{
			friendlyURL = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, isPrivateLayout,
					landingPageValue).getFriendlyURL();
		} catch (PortalException e)
		{
			LOG.error("Error in getting page friendlyURL of value mentioned in custom attribute : "
					+ landingPageValue);
			LOG.error(e.getMessage(), e);
		} catch (SystemException e)
		{
			LOG.error("Error in getting page friendlyURL of value mentioned in custom attribute : "
					+ landingPageValue);
			LOG.error(e.getMessage(), e);
		}
		return friendlyURL;
	}

	/**
	 * @param organization
	 * @param companyId
	 * @param isPrivateLayout
	 * @return
	 */
	public static String getLandingPageFriendlyURL(final Organization organization,
			final long companyId, final Boolean isPrivateLayout)
	{
		String landingPageFriendlyURL = StringPool.BLANK;

		if (Validator.isNotNull(organization))
		{
			String landingPageKey = getLandingPageKey(companyId, isPrivateLayout);
			String landingPageValue = (String) organization.getExpandoBridge().getAttribute(
					landingPageKey, Boolean.FALSE);
			if (Validator.isNotNull(landingPageValue))
			{
				landingPageFriendlyURL = CustomLandingPageUtil.getLayoutFriendlyURL(
						landingPageValue, organization.getGroupId(), isPrivateLayout);
			} else
			{
				LOG.debug("Either no Custom Attribute found with key " + landingPageKey + " in "
						+ organization.getName() + " organization OR it is having null/blank value");
			}
		}

		return landingPageFriendlyURL;
	}

	/**
	 * @param organization
	 * @param companyId
	 * @param isPrivateLayout
	 * @return
	 */
	public static String getLandingPageFriendlyURL(final Group group, final long companyId,
			final Boolean isPrivateLayout)
	{
		String landingPageFriendlyURL = StringPool.BLANK;

		if (Validator.isNotNull(group))
		{
			String landingPageKey = getLandingPageKey(companyId, isPrivateLayout);
			String landingPageValue = (String) group.getExpandoBridge().getAttribute(
					landingPageKey, Boolean.FALSE);
			if (Validator.isNotNull(landingPageValue))
			{
				landingPageFriendlyURL = CustomLandingPageUtil.getLayoutFriendlyURL(
						landingPageValue, group.getGroupId(), isPrivateLayout);
			} else
			{
				LOG.debug("Either no Custom Attribute found with key " + landingPageKey + " in "
						+ group.getName() + " site OR it is having null/blank value");
			}
		}

		return landingPageFriendlyURL;
	}

	/**
	 * Method will get the Landing Page key that will be used as key to fetch site/organization's
	 * custom attribute
	 * 
	 * @param isPrivateLayout
	 * @return Landing Page Key for Custom Attribute
	 */
	public static String getLandingPageKey(final long companyId, final Boolean isPrivateLayout)
	{
		String landingPageKey = CustomLandingPageConstant.LANDING_PAGE_KEY_DEFAULT_VALUE;
		try
		{
			landingPageKey = PrefsPropsUtil.getString(companyId,
					CustomLandingPageConstant.CUSTOM_LANDING_PAGE_KEY, landingPageKey);
			landingPageKey = landingPageKey
					+ (isPrivateLayout ? CustomLandingPageConstant.PRIVATE
							: CustomLandingPageConstant.PUBLIC);
		} catch (SystemException e)
		{
			LOG.error("Error in getting fetching "
					+ CustomLandingPageConstant.CUSTOM_LANDING_PAGE_KEY
					+ " value from portal.properties file", e);
		}

		return landingPageKey;
	}

	private static final Log LOG = LogFactoryUtil.getLog(CustomLandingPageUtil.class);
}
