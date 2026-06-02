# toumi
A tool for extracting necessary prototype declarations from C header files


## Example

```
# build
$ sbt toumi/nativeLink
...

# extract definitions by regex pattern
$ toumi/target/scala-3.8.3/toumi $(pkg-config --cflags gio-2.0) -t gio/gio.h -p 'g_variant_get_int.*' -p '.*signal_emit.*' -o extracted.h
...

# show generated header file
$ cat extracted.h 
extern   gint16   g_variant_get_int16 ( GVariant  * value );
extern   gint32   g_variant_get_int32 ( GVariant  * value );
extern   gint64   g_variant_get_int64 ( GVariant  * value );
extern   void   g_signal_emitv ( const   GValue  * instance_and_params ,  guint   signal_id ,  GQuark   detail ,  GValue  * return_value );
extern   void   g_signal_emit_valist ( gpointer instance ,  guint   signal_id ,  GQuark   detail ,  va_list   var_args );
extern   void   g_signal_emit ( gpointer instance ,  guint   signal_id ,  GQuark   detail , ...);
extern   void   g_signal_emit_by_name ( gpointer instance ,  const   gchar  * detailed_signal , ...);
```
