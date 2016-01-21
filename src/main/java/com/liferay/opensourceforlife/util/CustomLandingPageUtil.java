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
import com.liferay.portal.model.Role;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;

/**
 * @author Tejas Kanani
 */
public final class CustomLandingPageUtil
{

	/**
	 * 
	 */
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
	 * Generate displayURL for current user's user group
	 * 
	 * @param request
	 * @param isPrivateLayout
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getUserDisplayURL(final HttpServletRequest request,
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
			displayURL = getGroupFriendlyURL(request, group, isPrivateLayout, Boolean.TRUE);
		}

		return displayURL;
	}

	/**
	 * @param request
	 * @param currentGroup
	 * @param isPrivate
	 * @param isUser
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static String getGroupFriendlyURL(final HttpServletRequest request,
			final Group currentGroup, final boolean isPrivate, final boolean isUser)
			throws PortalException, SystemException
	{
		String friendlyURL = null;

		if (isPrivate)
		{
			if (isUser)
			{
				friendlyURL = CustomLandingPageConstant.PRIVATE_USER_SERVLET_MAPPING;
			} else
			{
				friendlyURL = CustomLandingPageConstant.PRIVATE_GROUP_SERVLET_MAPPING;
			}
		} else
		{
			friendlyURL = CustomLandingPageConstant.PUBLIC_GROUP_SERVLET_MAPPING;
		}

		StringBundler sb = new StringBundler(CustomLandingPageConstant.FOUR);

		sb.append(CustomLandingPageConstant.PORTAL_CONTEXT);
		sb.append(CustomLandingPageUtil.getLanguage(request));

		sb.append(friendlyURL);
		sb.append(currentGroup.getFriendlyURL());

		return sb.toString();
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
	 * @param landingPageKey
	 * @param landingPageValue
	 * @param groupId
	 * @param isPrivateLayout
	 * @return
	 */
	public static String getLayoutFriendlyURL(final String landingPageKey,
			final String landingPageValue, final long groupId, final boolean isPrivateLayout)
	{
		String friendlyURL = StringPool.BLANK;
		try
		{
			friendlyURL = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, isPrivateLayout,
					landingPageValue).getFriendlyURL();
		} catch (PortalException e)
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("No such page exist with frientlyURL : " + landingPageValue
						+ " , provided in " + landingPageKey + " custom attribute");
				LOG.error(e.getMessage(), e);
			}
		} catch (SystemException e)
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("No such page exist with frientlyURL : " + landingPageValue
						+ " , provided in " + landingPageKey + " custom attribute");
				LOG.error(e.getMessage(), e);
			}
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
			String landingPageKey = getLandingPageKey(companyId, isPrivateLayout, Boolean.TRUE);
			if (Validator.isNotNull(landingPageKey))
			{
				String landingPageValue = getExpandoValue(organization.getExpandoBridge(),
						landingPageKey, Boolean.FALSE);
				if (Validator.isNotNull(landingPageValue))
				{
					landingPageFriendlyURL = CustomLandingPageUtil.getLayoutFriendlyURL(
							landingPageKey, landingPageValue, organization.getGroupId(),
							isPrivateLayout);
				} else
				{
					LOG.debug("Custom Attribute with key " + landingPageKey + " in "
							+ organization.getName() + " organization is having null/blank value");
				}
			}
		}

		return landingPageFriendlyURL;
	}

	/**
	 * @param group
	 * @param expandoBridge
	 * @param companyId
	 * @param isPrivateLayout
	 * @return
	 */
	public static String getLandingPageFriendlyURL(final Group group, final ExpandoBridge expandoBridge, final long companyId,
			final Boolean isPrivateLayout)
	{
		String landingPageFriendlyURL = StringPool.BLANK;

		if (Validator.isNotNull(group))
		{
			String landingPageKey = getLandingPageKey(companyId, isPrivateLayout, Boolean.TRUE);

			if (Validator.isNotNull(landingPageKey))
			{
				String landingPageValue = getExpandoValue(expandoBridge, landingPageKey,
						Boolean.FALSE);
				if (Validator.isNotNull(landingPageValue))
				{
					landingPageFriendlyURL = CustomLandingPageUtil.getLayoutFriendlyURL(
							landingPageKey, landingPageValue, group.getGroupId(), isPrivateLayout);
				} else
				{
					LOG.debug("Custom Attribute found with key " + landingPageKey + " in "
							+ group.getName() + " site is having null/blank value");
				}
			}
		}
		return landingPageFriendlyURL;
	}

	/**
	 * @param role
	 * @param companyId
	 * @return
	 */
	public static String getLandingPageFriendlyURL(final Role role, final long companyId)
	{
		String landingPageFriendlyURL = StringPool.BLANK;

		if (Validator.isNotNull(role))
		{
			String landingPageKey = getLandingPageKey(companyId, Boolean.FALSE, Boolean.FALSE);
			if (Validator.isNotNull(landingPageKey))
			{
				String landingPageValue = getExpandoValue(role.getExpandoBridge(), landingPageKey,
						Boolean.FALSE);
				if (Validator.isNotNull(landingPageValue))
				{
					landingPageFriendlyURL = landingPageValue;
				} else
				{
					LOG.debug("Custom Attribute found with key " + landingPageKey + " in "
							+ role.getName() + " Role is having null/blank value");
				}
			}
		}
		return landingPageFriendlyURL;
	}

	/**
	 * @param userGroup
	 * @param companyId
	 * @return
	 */
	public static String getLandingPageFriendlyURL(final UserGroup userGroup, final long companyId)
	{
		String landingPageFriendlyURL = StringPool.BLANK;

		if (Validator.isNotNull(userGroup))
		{
			String landingPageKey = getLandingPageKey(companyId, Boolean.FALSE, Boolean.FALSE);
			if (Validator.isNotNull(landingPageKey))
			{
				String landingPageValue = getExpandoValue(userGroup.getExpandoBridge(),
						landingPageKey, Boolean.FALSE);
				if (Validator.isNotNull(landingPageValue))
				{
					landingPageFriendlyURL = landingPageValue;
				} else
				{
					LOG.debug("Custom Attribute found with key " + landingPageKey + " in "
							+ userGroup.getName() + " User Group is having null/blank value");
				}
			}
		}
		return landingPageFriendlyURL;
	}

	/**
	 * Method will return expando attribute value for given attributeName
	 * 
	 * @param expandoBridge
	 * @param attributeName
	 * @param isSecure
	 * @return
	 */
	private static String getExpandoValue(final ExpandoBridge expandoBridge,
			final String attributeName, final Boolean isSecure)
	{
		String expandoValue = null;
		if (Validator.isNotNull(attributeName) && expandoBridge.hasAttribute(attributeName))
		{
			expandoValue = (String) expandoBridge.getAttribute(attributeName, isSecure);
		} else
		{
			LOG.debug("No Custom Attribute found with key " + attributeName);
		}

		return expandoValue;
	}

	/**
	 * Method will get the Landing Page key that will be used as key to fetch Site,Organization,Role
	 * OR User Group's custom attribute
	 * 
	 * @param isPrivateLayout
	 * @param isTypeLayout
	 *            - True if getting landingPageKey for Site OR Organization & False if getting
	 *            landingPage for Role OR User Group
	 * @return Landing Page Key for Custom Attribute
	 */
	public static String getLandingPageKey(final long companyId, final Boolean isPrivateLayout,
			final Boolean isTypeLayout)
	{
		String landingPageKey = CustomLandingPageConstant.LANDING_PAGE_KEY_DEFAULT_VALUE;
		try
		{
			landingPageKey = PrefsPropsUtil.getString(companyId,
					CustomLandingPageConstant.CUSTOM_LANDING_PAGE_KEY, landingPageKey);
			if (isTypeLayout)
			{
				landingPageKey = landingPageKey
						+ (isPrivateLayout ? CustomLandingPageConstant.PRIVATE
								: CustomLandingPageConstant.PUBLIC);
			}
		} catch (SystemException e)
		{
			if (LOG.isErrorEnabled())
			{
				LOG.error("Error in fetching " + CustomLandingPageConstant.CUSTOM_LANDING_PAGE_KEY
						+ " value from portal.properties file");
				LOG.error(e.getMessage(), e);
			}
		}
		return landingPageKey;
	}

	public static Boolean getIncludeSystemRole()
	{
		boolean includeSystemRole = Boolean.FALSE;
		try
		{
			includeSystemRole = PrefsPropsUtil
					.getBoolean(CustomLandingPageConstant.INCLUDE_SYSTEM_ROLE_KEY);
		} catch (SystemException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return includeSystemRole;
	}

	public static Boolean getIncludeSystemUserGroup()
	{
		boolean includeSystemRole = Boolean.FALSE;
		try
		{
			includeSystemRole = PrefsPropsUtil
					.getBoolean(CustomLandingPageConstant.INCLUDE_SYSTEM_ROLE_KEY);
		} catch (SystemException e)
		{
			LOG.error(e.getMessage(), e);
		}

		return includeSystemRole;
	}

	private static final Log LOG = LogFactoryUtil.getLog(CustomLandingPageUtil.class);
}
