package me.ready.e;

/**
 * 项目中的通用异常类，用于表示数据(库)访问过程中引发的异常
 * @author Ready
 * @date 2012-4-23
 */
public class JdbcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造具有指定异常信息的数据访问异常实例
	 * @param message 指定的异常信息
	 */
	public JdbcException(String message) {
		super(message);
	}

	/**
	 * 构造具有指定异常源(导致该异常的异常)的数据访问异常实例
	 * @param cause 指定的异常源
	 */
	public JdbcException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造具有指定异常信息和异常源(导致该异常的异常)的数据访问异常实例
	 * @param message 指定的异常信息
	 * @param cause 指定的异常源
	 */
	public JdbcException(String message, Throwable cause) {
		super(message, cause);
	}
}
