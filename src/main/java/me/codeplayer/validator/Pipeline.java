package me.codeplayer.validator;

import java.util.function.*;

import javax.annotation.*;

import me.codeplayer.util.*;

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
	protected transient Object result = OK;
	protected transient boolean silent;

	public Pipeline(T bean) {
		this.bean = bean;
	}

	public <N, E> Pipeline<N, E> begin(N newBean, Function<? super N, E> getter, @Nullable BiConsumer<? super N, E> setter) {
		this.result = OK;
		this.bean = X.castType(newBean);
		this.getter = X.castType(getter);
		this.setter = X.castType(setter);
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
		this.result = OK;
		this.getter = X.castType(getter);
		this.setter = X.castType(setter);
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

	public Pipeline<T, R> apply(Function<? super R, R> validator) {
		if (canNext()) {
			R val = getter.apply(bean);
			if (silent) {
				try {
					val = validator.apply(val);
				} catch (Throwable e) {
					result = e;
				}
			} else {
				val = validator.apply(val);
			}
			if (setter != null) {
				setter.accept(bean, val);
			}
		}
		return this;
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
		}
		return this;
	}

	protected Pipeline<T, R> assertInternal(Supplier<Boolean> validator, final @Nullable Supplier<?> throwsError) {
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
			}
			if (!silent && !isOK()) {
				tryThrow(null, result);
			}
		}
		return this;
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(getter.apply(bean)), throwsError);
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator, final @Nullable CharSequence charSequence) {
		return asserts(validator, charSequence == null ? null : () -> charSequence);
	}

	public Pipeline<T, R> asserts(Predicate<? super R> validator) {
		return asserts(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(getter.apply(bean)), throwsError);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator, final @Nullable CharSequence charSequence) {
		return assertsNot(validator, charSequence == null ? null : () -> charSequence);
	}

	public Pipeline<T, R> assertsNot(Predicate<? super R> validator) {
		return assertsNot(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> expect(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> validator.test(bean), throwsError);
	}

	public Pipeline<T, R> expect(Predicate<? super T> validator, final @Nullable CharSequence charSequence) {
		return expect(validator, charSequence == null ? null : () -> charSequence);
	}

	public Pipeline<T, R> expect(Predicate<? super T> validator) {
		return expect(validator, (Supplier<?>) null);
	}

	public Pipeline<T, R> expectNot(Predicate<? super T> validator, final @Nullable Supplier<?> throwsError) {
		return assertInternal(() -> !validator.test(bean), throwsError);
	}

	public Pipeline<T, R> expectNot(Predicate<? super T> validator, final @Nullable CharSequence charSequence) {
		return expectNot(validator, charSequence == null ? null : () -> charSequence);
	}

	public Pipeline<T, R> expectNot(Predicate<? super T> validator) {
		return expectNot(validator, (Supplier<?>) null);
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
