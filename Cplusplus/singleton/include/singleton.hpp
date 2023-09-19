#include <memory>

namespace util
{
    template <typename T>
    class singleton
    {
    public:
        static const std::shared_ptr<T> instance();

        singleton(const singleton &) = delete;
        singleton &operator=(const singleton) = delete;

    protected:
        singleton() = default;

    public:
    };

    template <typename T>
    inline const std::shared_ptr<T> singleton<T>::instance()
    {
        static const std::shared_ptr<T> INSTANCE = std::make_shared<T>();
        return INSTANCE;
    }
}