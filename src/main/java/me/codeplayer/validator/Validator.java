package me.codeplayer.validator;

import java.util.function.*;
import javax.annotation.Nullable;

import me.codeplayer.util.X;

/**
 * 对实体及其属性进行预处理、校验、格式化的流水线处理封装类
 */
public class Validator<T, R> implements PropertyAccessor<T, R> {

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

	public Validator(T bean) {
		this.bean = bean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <N, E> Validator<N, E> begin(N newBean, Function<? super N, E> getter, @Nullable BiConsumer<? super N, E> setter) {
		final Validator raw = this;
		raw.bean = newBean;
		return raw.begin(getter, setter);
	}

	public <N, E> Validator<N, E> begin(N newBean, Function<? super N, E> getter) {
		return begin(newBean, getter, null);
	}

	public <N> Validator<N, R> begin(N newBean) {
		return begin(newBean, null, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <E> Validator<T, E> begin(@Nullable Function<? super T, E> getter, @Nullable BiConsumer<? super T, E> setter) {
		final Validator raw = this;
		raw.getter = getter;
		raw.setter = setter;
		raw.resetForNewProperty();
		return raw;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <E> Validator<T, E> beginValue(E val) {
		final Validator raw = this;
		raw.getter = t -> this.val;
		raw.setter = null;
		raw.result = OK;
		raw.val = val;
		return raw;
	}

	public <E> Validator<T, E> begin(PropertyAccessor<? super T, E> accessor) {
		return begin(accessor.getGetter(), accessor.getSetter());
	}

	public <E> Validator<T, E> begin(Function<? super T, E> getter) {
		return begin(getter, (BiConsumer<? super T, E>) null);
	}

	public Validator<T, R> silent(boolean silent) {
		this.silent = silent;
		return this;
	}

	public Validator<T, R> silent() {
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
		// 如果改动本方法，则需同步改动 beginValue() 方法
		this.result = OK;
		this.val = getter == null || (bean == null && silent) ? null : getter.apply(bean);
	}

	public Validator<T, R> apply(Function<? super R, R> callback) {
		if (canNext()) {
			R val = this.val;
			if (silent) {
				try {
					val = callback.apply(val);
				} catch (Throwable e) {
					result = e;
				}
			} else {
				val = callback.apply(val);
			}
			if (setter != null && val != this.val) {
				setter.accept(bean, this.val = val);
			}
		}
		return this;
	}

	public Validator<T, R> applyIf(Predicate<? super R> precondition, Function<? super R, R> callback) {
		return precondition.test(val) ? this.apply(callback) : this;
	}

	public Validator<T, R> applyIfBean(Predicate<? super T> precondition, Function<? super R, R> callback) {
		return precondition.test(bean) ? this.apply(callback) : this;
	}

	public Validator<T, R> accept(Consumer<? super T> beanConsumer) {
		if (canNext()) {
			if (silent) {
				try {
					beanConsumer.accept(bean);
				} catch (Throwable e) {
					result = e;
				}
			} else {
				beanConsumer.accept(bean);
			}
			if (getter != null) { // ensure val is refresh
				this.val = getter.apply(bean);
			}
		}
		return this;
	}

	public Validator<T, R> acceptIf(Predicate<? super R> precondition, Consumer<? super T> callback) {
		return precondition.test(val) ? this.accept(callback) : this;
	}

	public Validator<T, R> acceptIfBean(Predicate<? super T> precondition, Consumer<? super T> callback) {
		return precondition.test(bean) ? this.accept(callback) : this;
	}

	protected Validator<T, R> assertInternal(Supplier<Boolean> validator, @Nullable Object throwsError) {
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

	public Validator<T, R> asserts(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(getter.apply(bean)), throwsError);
	}

	public Validator<T, R> asserts(Predicate<? super R> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> validator.test(getter.apply(bean)), errorMsg);
	}

	public Validator<T, R> asserts(Predicate<? super R> validator) {
		return asserts(validator, (Supplier<?>) null);
	}

	public Validator<T, R> assertsNot(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(getter.apply(bean)), throwsError);
	}

	public Validator<T, R> assertsNot(Predicate<? super R> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> !validator.test(getter.apply(bean)), errorMsg);
	}

	public Validator<T, R> assertsNot(Predicate<? super R> validator) {
		return assertsNot(validator, (Supplier<?>) null);
	}

	public Validator<T, R> assertBean(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(bean), throwsError);
	}

	public Validator<T, R> assertBean(Predicate<? super T> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> validator.test(bean), errorMsg);
	}

	public Validator<T, R> assertBean(Predicate<? super T> validator) {
		return assertBean(validator, (Supplier<?>) null);
	}

	public Validator<T, R> assertBeanNot(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(bean), throwsError);
	}

	public Validator<T, R> assertBeanNot(Predicate<? super T> validator, final @Nullable CharSequence errorMsg) {
		return assertInternal(() -> !validator.test(bean), errorMsg);
	}

	public Validator<T, R> assertBeanNot(Predicate<? super T> validator) {
		return assertBeanNot(validator, (Supplier<?>) null);
	}

	public Validator<T, R> tryThrow(@Nullable Supplier<?> toThrow) {
		if (!isOK()) {
			tryThrow(toThrow, result);
		}
		return this;
	}

	public Validator<T, R> tryThrow(@Nullable CharSequence errorMsg) {
		if (!isOK()) {
			tryThrow(errorMsg, result);
		}
		return this;
	}

	public Validator<T, R> tryThrow() {
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

	public static <T, R> Validator<T, R> of(T bean, Function<? super T, R> getter, @Nullable BiConsumer<? super T, R> setter) {
		return new Validator<>(bean).begin(getter, setter);
	}

	public static <T, R> Validator<T, R> of(T bean, Function<? super T, R> getter) {
		return new Validator<>(bean).begin(getter, (BiConsumer<? super T, R>) null);
	}

	public static <T, R> Validator<T, R> of(T bean, PropertyAccessor<? super T, R> accessor) {
		return new Validator<>(bean).begin(accessor);
	}

	public static <T, R> Validator<T, R> of(T bean) {
		return new Validator<>(bean);
	}

	public static <R> Validator<R, R> valueOf(R val) {
		return of(val).beginValue(val);
	}

}