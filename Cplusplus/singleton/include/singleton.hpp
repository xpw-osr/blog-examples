#include <memory>

namespace util
{
    template <typename T>
    class singleton
    {
    public:
        static const std::unique_ptr<T> &instance();

        singleton(const singleton &) = delete;
        singleton &operator=(const singleton) = delete;

    protected:
        singleton() = default;

    public:
    };

    template <typename T>
    inline const std::unique_ptr<T> &singleton<T>::instance()
    {
        static const std::unique_ptr<T> &INSTANCE = std::make_unique<T>();
        return INSTANCE;
    }
}