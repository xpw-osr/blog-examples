#include <iostream>
#include <queue>
#include <thread>
#include <chrono>
#include <a.hpp>

int main(void)
{
    util::singleton<A>::instance()->a = 1;
    std::cout << util::singleton<A>::instance()->a << std::endl;
    util::singleton<A>::instance()->a = 3;
    std::cout << util::singleton<A>::instance()->a << std::endl;

    std::queue<std::thread> threads;
    for (int x = 0; x < 10; x++)
    {
        threads.push(std::thread {[]()
                                {
                                    std::this_thread::sleep_for(std::chrono::microseconds(100));
                                    util::singleton<A>::instance()->a += 1;
                                    std::cout << util::singleton<A>::instance()->a << std::endl;
                                }});
    }

    for (int x = 0; x < 10; x++)
    {
        auto t = std::move(threads.front());
        threads.pop();

        if (t.joinable())
        {
            t.join();
        }
    }

    std::this_thread::sleep_for(std::chrono::seconds(2));
    std::cout << "quit" << std::endl;
    return 0;
}
