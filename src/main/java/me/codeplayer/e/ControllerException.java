package me.codeplayer.e;

/**
 * 项目中的通用异常类，用于表示控制器处理过程中引发的运行时异常
 * @author Ready
 * @date 2012-4-23
 */
public class ControllerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造具有指定异常信息的控制器异常实例
	 * @param message 指定的异常信息
	 */
	public ControllerException(String message) {
		super(message);
	}

	/**
	 * 构造具有指定异常源(导致该异常的异常)的控制器异常实例
	 * @param cause 指定的异常源
	 */
	public ControllerException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造具有指定异常信息和异常源(导致该异常的异常)的控制器异常实例
	 * @param message 指定的异常信息
	 * @param cause 指定的异常源
	 */
	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}
}
