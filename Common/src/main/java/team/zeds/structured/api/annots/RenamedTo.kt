package team.zeds.structured.api.annots

import kotlin.annotation.AnnotationTarget.*

/**
 * Annotation [RenamedTo] sets how the method will be called in a particular version, which is specified in the "since" parameter
 */
@Target(
    CLASS,
    ANNOTATION_CLASS,
    TYPE_PARAMETER,
    PROPERTY,
    FIELD,
    LOCAL_VARIABLE,
    VALUE_PARAMETER,
    CONSTRUCTOR,
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
    TYPE,
    EXPRESSION,
    FILE,
    TYPEALIAS
)
@Retention(AnnotationRetention.SOURCE)
annotation class RenamedTo(val value: String, val since: String = "")
