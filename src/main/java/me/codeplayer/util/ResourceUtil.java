package me.codeplayer.util;

/**
 * 用于辅助URL、URI、资源路径等处理的工具类
 * 
 * @since 1.0
 * @author Ready
 * @date 2019年4月16日
 */
public abstract class ResourceUtil {

	/**
	 * 解析指定资源路径的定位方式
	 * 
	 * @param uri
	 * @return -1=相对（于当前资源路径）定位；0=相对（于当前项目根路径）定位；1=绝对定位
	 */
	public static final int parseUriLocator(String uri) {
		// 如果是"/path/of/file" => "${root}/path/of/file"
		final int len = uri.length();
		if (len > 0) {
			final char firstChar = uri.charAt(0);
			// 如果是"${protocol:}//path/of/file"，则直接输出
			if (firstChar == '/') {
				if (len == 1 || uri.charAt(1) != '/') {
					return 0;
				} else {
					return 1;
				}
			} else if (firstChar != '#' && firstChar != '?' && len > 3) {
				// .i.e: "https://"、"http://"、"ftp://"、"file://"
				int end = Math.min(len, 10), // 出于性能考虑，最多遍历前10个字符（无法100%兼容所有协议，但已经兼容了绝大部分协议头）
						flag = 0;
				for (int i = 1; i < end; i++) {
					final char ch = uri.charAt(i);
					if (ch == ':') {
						flag = 1;
					} else if (ch == '/' && flag > 0) {
						flag++;
					} else if (flag > 0 || ch == '?' || ch == '#') {
						break;
					} else {
						flag = 0;
					}
				}
				if (flag >= 3) {
					return 1;
				}
			}
		}
		return -1;
	}
}
