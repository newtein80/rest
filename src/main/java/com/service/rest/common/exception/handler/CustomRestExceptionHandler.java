package com.service.rest.common.exception.handler;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.google.common.base.Strings;
import com.service.rest.common.exception.exceptions.AbstractRestBaseException;
import com.service.rest.common.exception.exceptions.RestBadRequestException;
import com.service.rest.common.exception.exceptions.RestInternalServerException;
import com.service.rest.common.exception.exceptions.RestMethodNotAllowedException;
import com.service.rest.common.exception.exceptions.RestNotFoundException;
import com.service.rest.common.exception.exceptions.RestUnauthorizedException;
import com.service.rest.common.response.RestErrorStatus;
import com.service.rest.common.response.RestResponseMessage;
import com.service.rest.security.exception.TokenRefreshException;
import com.service.rest.security.exception.UserNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <p> Exception 발생시 핸들링하고 Custom Exception Class에 Mapping
 * @see <a href=
 *      "https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">https://www.baeldung.com/global-error-handler-in-a-spring-rest-api</a>
 * @author ParkJongSoo
 * @version 1
 * @since 2020.03.24
 */
@RestControllerAdvice // ! 중요
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // todo: request audit entity & jpa --> save
    // @Resource(name = "RestMapper")
    // private RestMapper restMapper;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) throws RestInternalServerException {
        
        Map<String, Object> paramMap = new HashMap<String, Object>();

    	try {
    		RestResponseMessage _apiMsg = (RestResponseMessage) body;
    		
            HttpServletRequest _httpServletRequest = ((ServletWebRequest) request).getRequest();

            int seq = (Integer) _httpServletRequest.getAttribute("REQUESTID");
            int response_detail_code = 0;
            response_detail_code = _apiMsg.getError().getCode();

    		paramMap.put("seq", seq);
    		paramMap.put("httpstatus", status.name());
    		paramMap.put("response_code", status.value());
    		paramMap.put("response_detail_code", response_detail_code);
    		paramMap.put("class_name", ex.getClass().getSimpleName());
    		paramMap.put("response_message", StringUtils.join(_apiMsg.getError().getErrorMessage(), ","));
    		
            // todo: request audit entity & jpa --> save
            // restMapper.updateApiLog(paramMap);
            
		} catch (Exception e) {
			return super.handleExceptionInternal(e, body, headers, status, request);
		}

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        // todo: MethodArgumentNotValidException, BindException 은 @Valid Annotation으로 Request 데이터의 Validation이 실패했을 때
        // todo: BindException = Validated by @Valid
        // todo: MethodArgumentNotValidException = Validated by @Validated for @RequestParam validatation
        List<String> errors = new ArrayList<String>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        AbstractRestBaseException _exception = new RestBadRequestException(errors, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), errors);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        // todo: MethodArgumentNotValidException, BindException 은 @Valid Annotation으로 Request 데이터의 Validation이 실패했을 때
        // todo: BindException = Validated by @Valid
        // todo: MethodArgumentNotValidException = Validated by @Validated for @RequestParam validatation
        List<String> errors = new ArrayList<String>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        AbstractRestBaseException _exception = new RestBadRequestException(errors, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), errors);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String error = ex.getMostSpecificCause().getLocalizedMessage();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String error = ex.getRequestPartName().toString();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        String error = ex.getParameterName().toString() + " parameter is missing";

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @ExceptionHandler({ ConstraintViolationException.class }) // ! 지정한 Exception 발생 시 해당 ExceptionHandler가 동작함
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }

        AbstractRestBaseException _exception = new RestBadRequestException(errors, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errors);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleConstraintViolation(MethodArgumentTypeMismatchException ex, WebRequest request) {

        String fieldName = ex.getName();
        String definedTypeClass = ex.getRequiredType().getName();
        String requestedTypeClass = ex.getValue().getClass().getSimpleName();
        String error = fieldName + " ( " + definedTypeClass + " ) should be of type " + requestedTypeClass;

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getLocalizedMessage();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getLocalizedMessage();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getPropertyName() + " = " + ex.getRequiredType();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getMostSpecificCause().getLocalizedMessage();

        AbstractRestBaseException _exception = new RestBadRequestException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        AbstractRestBaseException _exception = new RestNotFoundException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    /*
     * (non-Javadoc)
     * https://stackoverflow.com/questions/56459170/spring-controlleradvice-fail-to-
     * override-handlehttprequestmethodnotsupported
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.
     * ResponseEntityExceptionHandler#handleHttpRequestMethodNotSupported(org.
     * springframework.web.HttpRequestMethodNotSupportedException,
     * org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
     * org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        String error = builder.toString();

        AbstractRestBaseException _exception = new RestMethodNotAllowedException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = ex.getMostSpecificCause().getLocalizedMessage();
        
        AbstractRestBaseException _exception = new RestMethodNotAllowedException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        String error = builder.toString();

        AbstractRestBaseException _exception = new RestMethodNotAllowedException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(status, status.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }
    
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request, final HttpServletRequest req) throws Exception {

        String error = "[INTERNAL_SERVER_ERROR]: " + (StringUtils.isAnyEmpty(ex.getLocalizedMessage())? ex.getClass().getName() : ex.getLocalizedMessage());

    	AbstractRestBaseException _exception = new RestInternalServerException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    // @ExceptionHandler({ AccessDeniedException.class })
    // public ResponseEntity<Object> handleAccessDeniedException(final AccessDeniedException ex, final WebRequest request, final HttpServletRequest req, HttpServletResponse httpServletResponse) throws Exception {

    //     String error = "You do not have access roles.";

    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     SecurityMember member = (SecurityMember)authentication.getPrincipal();
    //     Collection<GrantedAuthority> authorities = member.getAuthorities().stream().map(
    //         // role -> new SimpleGrantedAuthority(((Principal) role).getName())
    //         role -> new SimpleGrantedAuthority(role.getAuthority())
    //     )
    //     .collect(Collectors.toList());

    //     if (hasRole(authorities, UserRole.ROLE_NOT_PERMITTED.name())) {
    //         error = "You did not receive a user verification email.";
    //     }

    // 	AbstractRestBaseException _exception = new RestUnauthorizedException(error, ex);
    //     RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), error);

    //     return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    // }

    private boolean hasRole(Collection<GrantedAuthority> authorites, String role){
        return authorites.contains(new SimpleGrantedAuthority(role));
    }

    @ExceptionHandler({ SQLException.class })
    public ResponseEntity<Object> handleSQLException(final SQLException ex, final WebRequest request, final HttpServletRequest req) throws Exception {

        String error = ex.getSQLState();

        AbstractRestBaseException _exception = new RestInternalServerException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleBadRequest(final Exception ex, final WebRequest request, final HttpServletRequest req) throws Exception {

        String error = Strings.isNullOrEmpty(ex.getLocalizedMessage())? ex.getClass().getName() : ex.getLocalizedMessage();

        AbstractRestBaseException _exception = new RestInternalServerException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @ExceptionHandler({ UserNotFoundException.class, RestNotFoundException.class })
    public ResponseEntity<Object> handleUserNotFoundException(final UserNotFoundException ex, final WebRequest request, final HttpServletRequest req) throws Exception {

        String error = ex.getMessage();

        AbstractRestBaseException _exception = new RestNotFoundException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    @ExceptionHandler({ TokenRefreshException.class, RestUnauthorizedException.class })
    public ResponseEntity<Object> handleTokenRefreshException(final TokenRefreshException ex, final WebRequest request, final HttpServletRequest req) throws Exception {

        String error = ex.getMessage();

        AbstractRestBaseException _exception = new RestUnauthorizedException(error, ex);
        RestErrorStatus restErrorStatus = new RestErrorStatus(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), error);

        return sendToExceptionInternal(ex, request, _exception, restErrorStatus);
    }

    private ResponseEntity<Object> sendToExceptionInternal(final Exception ex, final WebRequest request, AbstractRestBaseException _exception, RestErrorStatus restErrorStatus) {
        _exception.setCode(restErrorStatus.getErrorcode());
        String _referUrl = ((ServletWebRequest) request).getRequest().getRequestURI().toString();
        final RestResponseMessage _obj = new RestResponseMessage(_exception, _referUrl, restErrorStatus.getHttpStatus());
        return handleExceptionInternal(ex, _obj, new HttpHeaders(), restErrorStatus.getHttpStatus(), request);
    }

	public static String checkValue(Object obj) throws Exception {
		String retValue = "";

		if(obj == null){
			return retValue;
		}else if(obj instanceof String){
			retValue = (String)obj;
		}else if(obj instanceof Integer){
			retValue = String.valueOf(obj);
		}else if(obj instanceof Long){
			retValue = String.valueOf(obj);
		}else if(obj instanceof BigDecimal){
			retValue = String.valueOf(obj);
		}else if(obj instanceof Timestamp){
			retValue = String.valueOf(obj);
		}else if(obj instanceof Clob){
			Clob clob = (Clob) obj;
			StringBuffer strOut = new StringBuffer();
			String str = "";
			BufferedReader br = new BufferedReader(clob.getCharacterStream());
			while ((str = br.readLine()) != null) {
                strOut.append(str);
			}
			retValue = strOut.toString();
		}else{
			retValue = (String)obj;
		}

		return retValue ;
	}
}