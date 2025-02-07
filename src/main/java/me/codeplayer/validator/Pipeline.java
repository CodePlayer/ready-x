package me.codeplayer.validator;

import java.util.function.*;
import javax.annotation.Nullable;

import me.codeplayer.util.X;

/**
 * 对实体及其属性进行预处理、校验、格式化的流水线处理封装类
 */
public class Pipeline<T, R> implements PropertyAccessor<T, R> {

	/** 用于标识当前 Pipeline 的校验结果为通过 */
	public static final Object OK = new Object();
	//
	protected transient T bean;
	protected transient Function<? super T, R> getter;
	protected transient BiConsumer<? super T, R> setter;
	protected transient R val;
	/**
	 * 用于存储校验结果 <p>
	 * 默认为 OK，也有可能为：
	 * <ul>
	 * <li>CharSequence => 一般是错误提示信息</li>
	 * <li>Supplier< ? > => 断言失败时才会触发的自定义 Supplier 对象</li>
	 * <li>Throwable => 抛出的异常对象</li>
	 * </ul>
	 */
	protected transient Object result = OK;
	protected transient boolean silent;

	public Pipeline(T bean) {
		this.bean = bean;
	}

	public <N, E> Pipeline<N, E> begin(N newBean, Function<? super N, E> getter, @Nullable BiConsumer<? super N, E> setter) {
		this.bean = X.castType(newBean);
		this.getter = X.castType(getter);
		this.setter = X.castType(setter);
		resetForNewProperty();
		return X.castType(this);
	}

	public <N, E> Pipeline<N, E> begin(N newBean, Function<? super N, E> getter) {
		return begin(newBean, getter, null);
	}

	public Pipeline<T, R> begin(T newBean) {
		this.bean = newBean;
		return this;
	}

	public <N, E> Pipeline<N, E> begin(Function<? super N, E> getter, @Nullable BiConsumer<? super N, E> setter) {
		this.getter = X.castType(getter);
		this.setter = X.castType(setter);
		resetForNewProperty();
		return X.castType(this);
	}

	public <N, E> Pipeline<N, E> begin(PropertyAccessor<? super N, E> accessor) {
		return begin(accessor.getGetter(), accessor.getSetter());
	}

	public <E> Pipeline<T, E> begin(Function<? super T, E> getter) {
		return begin(getter, (BiConsumer<? super T, E>) null);
	}

	public Pipeline<T, R> silent(boolean silent) {
		this.silent = silent;
		return this;
	}

	public Pipeline<T, R> silent() {
		return silent(true);
	}

	protected boolean canNext() {
		if (!isOK()) {
			return false;
		}
		if (silent && bean == null) {
			result = null;
			return false;
		}
		return true;
	}

	protected void resetForNewProperty() {
		this.result = OK;
		this.val = getter == null || (bean == null && silent) ? null : getter.apply(bean);
	}

	public Pipeline<T, R> apply(Function<? super R, R> validator) {
		if (canNext()) {
			R val = this.val;
			if (silent) {
				try {
					this.val = val = validator.apply(val);
				} catch (Throwable e) {
					result = e;
				}
			} else {
				this.val = val = validator.apply(val);
			}
			if (setter != null) {
				setter.accept(bean, val);
			}
		}
		return this;
	}

	public Pipeline<T, R> applyIf(Predicate<? super R> precondition, Function<? super R, R> validator) {
		return apply(val -> precondition.test(val) ? validator.apply(val) : val);
	}

	public Pipeline<T, R> apply(Consumer<? super T> validator) {
		if (canNext()) {
			if (silent) {
				try {
					validator.accept(bean);
				} catch (Throwable e) {
					result = e;
				}
			} else {
				validator.accept(bean);
			}
			if (getter != null) { // ensure val is refresh
				this.val = getter.apply(bean);
			}
		}
		return this;
	}

	public Pipeline<T, R> applyIf(Predicate<? super T> precondition, Consumer<? super T> validator) {
		return apply(t -> {
			if (precondition.test(t)) {
				validator.accept(t);
			}
		});
	}

	public Pipeline<T, R> applyIfValue(Consumer<? super T> validator, Predicate<? super R> precondition) {
		return apply(t -> {
			final R val = this.getter.apply(t);
			if (precondition.test(val)) {
				validator.accept(t);
			}
		});
	}

	public Pipeline<T, R> applyValueIf(final Predicate<? super R> precondition, final Consumer<? super R> validator) {
		return apply(val -> {
			if (precondition == null || precondition.test(val)) {
				validator.accept(val);
			}
			return val;
		});
	}

	public Pipeline<T, R> applyValue(Consumer<? super R> validator) {
		return applyValueIf(null, validator);
	}

	protected Pipeline<T, R> assertInternal(Supplier<Boolean> validator, @Nullable Object throwsError) {
		if (canNext()) {
			boolean matches;
			if (silent) {
				try {
					matches = validator.get();
				} catch (Throwable e) {
					result = e;
					matches = false;
				}
			} else {
				matches = validator.get();
			}
			if (!matches && isOK()) {
				result = throwsError;
				throwsError = null;
			}
			if (!silent && !isOK()) {
				tryThrow(throwsError, result);
			}
		}
		return this;
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(getter.apply(bean)), throwsError);
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> validator.test(getter.apply(bean)), errorMsg);
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator) {
		return asserts(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(getter.apply(bean)), throwsError);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> !validator.test(getter.apply(bean)), errorMsg);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator) {
		return assertsNot(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> assertBean(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(bean), throwsError);
	}

	public Pipeline<T, R> assertBean(Predicate<? super T> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> validator.test(bean), errorMsg);
	}

	public Pipeline<T, R> assertBean(Predicate<? super T> validator) {
		return assertBean(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> assertBeanNot(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(bean), throwsError);
	}

	public Pipeline<T, R> assertBeanNot(Predicate<? super T> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> !validator.test(bean), errorMsg);
	}

	public Pipeline<T, R> assertBeanNot(Predicate<? super T> validator) {
		return assertBeanNot(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> tryThrow(@Nullable Supplier<?> toThrow) {
		if (!isOK()) {
			tryThrow(toThrow, result);
		}
		return this;
	}

	public Pipeline<T, R> tryThrow(@Nullable CharSequence errorMsg) {
		if (!isOK()) {
			tryThrow(errorMsg, result);
		}
		return this;
	}

	public Pipeline<T, R> tryThrow() {
		if (!isOK()) {
			tryThrow(result, null);
		}
		return this;
	}

	public static void tryThrow(@Nullable Object toThrow, @Nullable Object result) {
		toThrow = X.tryUnwrap(toThrow);
		if (toThrow instanceof Throwable) {
			throw X.wrapException(null, (Throwable) toThrow);
		}
		String message = toThrow == null ? null : toThrow.toString();
		if (result != null) {
			result = X.tryUnwrap(result);
			if (result instanceof Throwable) {
				throw X.wrapException(message, (Throwable) result);
			} else if (message == null) {
				message = result.toString();
			}
		}
		throw X.wrapException(message, null);
	}

	public boolean isOK() {
		return result == OK;
	}

	public Object getResult() {
		Object error = result;
		if (error instanceof Supplier) {
			error = ((Supplier<?>) error).get();
		}
		return error;
	}

	public <E> E getResult(Class<E> expectType) {
		Object error = result;
		if (error == OK) {
			return null;
		}
		if (Supplier.class == expectType) {
			if (!(error instanceof Supplier)) {
				final Object ref = error;
				Supplier<?> wrapper = () -> ref;
				return X.castType(wrapper);
			}
			return X.castType(error);
		}
		if (error instanceof Supplier) {
			error = ((Supplier<?>) error).get();
		}
		if (String.class == expectType) {
			return X.castType(String.valueOf(error));
		}
		return expectType.cast(error);
	}

	@Override
	public Function<? super T, R> getGetter() {
		return getter;
	}

	@Override
	public BiConsumer<? super T, R> getSetter() {
		return setter;
	}

}